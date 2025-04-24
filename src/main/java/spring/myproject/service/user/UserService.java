package spring.myproject.service.user;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.common.async.AsyncService;
import spring.myproject.common.exception.meeting.NotAuthorizeException;
import spring.myproject.common.exception.user.*;
import spring.myproject.dto.request.user.UserRequestDto;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.certification.CertificationRepository;
import spring.myproject.utils.mapper.UserFactory;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.common.provider.JwtProvider;
import spring.myproject.common.s3.S3ImageUploadService;
import spring.myproject.common.validator.JwtValidator;

import java.io.IOException;
import java.util.List;

import static spring.myproject.dto.request.user.UserRequestDto.*;
import static spring.myproject.dto.response.user.UserResponseDto.*;
import static spring.myproject.utils.ConstClass.*;
import static spring.myproject.utils.CookieUtil.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;
    private final CertificationRepository certificationRepository;
    private final S3ImageUploadService s3ImageUploadService;
    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;
    private final AsyncService asyncService;
    @Value("${jwt.refresh.expiration}")
    private int refreshExpiration;
    @Value("${server.url}")
    private String url;


    public IdCheckResponse idCheck(IdCheckRequest idCheckRequest) {

        boolean idCheck = !userRepository.existsByUsername(idCheckRequest.getUsername());
        if(!idCheck) throw new ExistUserException("user Exist!!");
        return IdCheckResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
    public NicknameCheckResponse nicknameCheck(NicknameCheckRequest nicknameCheckRequest) {
        boolean nicknameCheck = !userRepository.existsByNickname(nicknameCheckRequest.getNickname());
        if(!nicknameCheck) throw new ExistUserException("user Exist!!");
        return NicknameCheckResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);

    }

    public SignUpResponse signUp(SignUpRequest signUpRequest, MultipartFile file) throws IOException {

            Image image = null;
            if(!file.isEmpty()) {
                String contentType = file.getContentType();
                String url = s3ImageUploadService.upload(file);
                image = Image.builder()
                        .contentType(contentType)
                        .url(url)
                        .build();
                imageRepository.save(image);
            }
            User user = UserFactory.toUser(signUpRequest,image,passwordEncoder);
            userRepository.save(user);
            return SignUpResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);

    }

    public UpdateResponse update(UpdateRequest updateRequest, Long userId,MultipartFile file,Long id) throws IOException {
            User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("user Not Found!"));
            boolean authorize = userId.equals(id);
            if(!authorize) throw new NotAuthorizeException("Not Authorize!");
            Image image = null;
            if(file != null && !file.isEmpty()){
                String contentType = file.getContentType();
                String url = s3ImageUploadService.upload(file);
                image = Image.builder()
                        .contentType(contentType)
                        .url(url)
                        .build();
                imageRepository.save(image);
                user.changeProfileImage(image);
            }
            user.change(updateRequest,passwordEncoder);

            return UpdateResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,userId);
    }

    public SignInResponse signIn(SignInRequest signInRequest, HttpServletResponse response) {

            User user = userRepository.findByUsername(signInRequest.getUsername()).orElseThrow(() -> new NotFoundUserException("not Found User"));
            boolean matches = passwordEncoder.matches(signInRequest.getPassword(), user.getPassword());
            if(!matches){
                throw new UnCorrectPasswordException("doesn't match Password!");
            }
            String accessToken = jwtProvider.createAccessToken(user);
            String refreshToken = jwtProvider.createRefreshToken(user);
            Cookie cookie = getCookie("refreshToken", refreshToken,refreshExpiration);
            response.addCookie(cookie);
            user.changeRefreshToken(refreshToken);
            return SignInResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,accessToken);
    }

    public EmailCertificationResponse emailCertification(EmailCertificationRequest emailCertificationRequest) {

            List<User> users = userRepository.findByEmail(emailCertificationRequest.getEmail());
            if(users.size()>1) throw new DuplicateEmailExeption("Duplicate Email");
            asyncService.asyncTask(emailCertificationRequest);
            return EmailCertificationResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }



    public GenerateTokenResponse generateToken(String refreshToken,HttpServletResponse response) {
        try {
            Claims claims = jwtValidator.validateToken(refreshToken);
            Long userId = (Long) claims.get("id");
            User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("not Found User"));
            String accessToken = jwtProvider.createAccessToken(user);
            String issuedRefreshToken = jwtProvider.createRefreshToken(user);
            user.changeRefreshToken(issuedRefreshToken);
            Cookie cookie = getCookie("refreshToken", refreshToken,refreshExpiration);
            response.addCookie(cookie);
            return GenerateTokenResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE, accessToken);

        }catch (Exception e){
            return GenerateTokenResponse.of(UN_VALID_TOKEN,UN_VALID_TOKEN_MESSAGE,null);
        }
    }


    public CheckCertificationResponse checkCertification(CheckCertificationRequest checkCertificationRequest) {
        String certification = certificationRepository.findCertificationByEmail(checkCertificationRequest.getEmail());
        if(!StringUtils.hasText(certification)) throw new NotFoundCertificationException("Not Found Certification");
        if(!certification.equals(checkCertificationRequest.getCertification())) throw new UnCorrectCertification("UnCorrect Certification");
        return CheckCertificationResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public UserResponse fetchUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("user Not Found!"));
        return UserResponse.from(SUCCESS_CODE,SUCCESS_MESSAGE,user,fileUrl -> url + fileUrl);
    }
}
