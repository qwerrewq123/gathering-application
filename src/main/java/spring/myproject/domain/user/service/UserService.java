package spring.myproject.domain.user.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.user.Role;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.dto.request.*;
import spring.myproject.domain.user.dto.response.*;
import spring.myproject.domain.user.exception.*;
import spring.myproject.domain.user.repository.UserRepository;
import spring.myproject.provider.EmailProvider;
import spring.myproject.provider.JwtProvider;
import spring.myproject.s3.S3ImageUploadService;

import java.util.List;

import static spring.myproject.util.ConstClass.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailProvider emailProvider;
    private final S3ImageUploadService s3ImageUploadService;
    private final JwtProvider jwtProvider;

    public IdCheckResponse idCheck(IdCheckRequest idCheckRequest) {

        boolean idCheck = !userRepository.existsByUsername(idCheckRequest.getUsername());
        if(!idCheck) throw new ExistUserException("user Exist!!");
        return IdCheckResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .build();
    }
    public NicknameCheckResponse nicknameCheck(NicknameCheckRequest nicknameCheckRequest) {
        boolean nicknameCheck = !userRepository.existsByNickname(nicknameCheckRequest.getNickname());
        if(!nicknameCheck) throw new ExistUserException("user Exist!!");
        return NicknameCheckResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .build();

    }

    public SignUpResponse signUp(UserRequest userRequest, MultipartFile file){

        try {
            Image image = null;
            if(!file.isEmpty()){
                String url = s3ImageUploadService.upload(file);
                image = Image.builder()
                        .url(url)
                        .build();
            }
            imageRepository.save(image);
            User user = User.builder()
                    .age(userRequest.getAge())
                    .email(userRequest.getEmail())
                    .hobby(userRequest.getHobby())
                    .address(userRequest.getAddress())
                    .username(userRequest.getUsername())
                    .password(passwordEncoder.encode(userRequest.getPassword()))
                    .role(Role.USER)
                    .profileImage(image)
                    .nickname(userRequest.getNickname())
                    .build();
            userRepository.save(user);
            return SignUpResponse.builder()
                    .code(SUCCESS_CODE)
                    .message(SUCCESS_MESSAGE)
                    .build();
        }catch (Exception e){
            log.error("error", e);
            return SignUpResponse.builder()
                    .code(DB_ERROR_CODE)
                    .message(DB_ERROR_MESSAGE)
                    .build();
        }
    }

    public SignInResponse signIn(SignInRequest signInRequest, HttpSession session) {

            User user = userRepository.findByUsername(signInRequest.getUsername()).orElseThrow(() -> new NotFoundUserException("not Found User"));
            boolean matches = passwordEncoder.matches(signInRequest.getPassword(), user.getPassword());
            if(!matches){
                throw new UnCorrectPasswordException("doesn't match Password!");
            }
            String accessToken = jwtProvider.createAccessToken(user.getUsername(),user.getRole().toString());
            String refreshToken = jwtProvider.createRefreshToken(user.getUsername(),user.getRole().toString());
            user.changeRefreshToken(refreshToken);
            return SignInResponse.builder()
                    .code(SUCCESS_CODE)
                    .message(SUCCESS_MESSAGE)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
    }

    public EmailCertificationResponse emailCertification(EmailCertificationRequest emailCertificationRequest) {

            List<User> users = userRepository.findByEmail(emailCertificationRequest.getEmail());
            if(users.size() == 0) throw new NotFoundEmailExeption("Not Found Email");
            if(users.size()>1) throw new DuplicateEmailExeption("Duplicate Email");
            emailProvider.sendCertificationMail(emailCertificationRequest.getEmail(), certificationNumber());
            return EmailCertificationResponse.builder()
                        .code(SUCCESS_CODE)
                        .message(SUCCESS_MESSAGE)
                        .build();
    }

    private String certificationNumber(){
        int number = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(number);
    }

    public UserResponse fetchUser(Long userId) {
        User foundUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("not Found User"));
        return UserResponse.of(foundUser);
    }
}
