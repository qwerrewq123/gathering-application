package spring.myproject.domain.user.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;
import spring.myproject.dto.request.user.EmailCertificationRequest;
import spring.myproject.dto.request.user.IdCheckRequest;
import spring.myproject.dto.request.user.SignInRequest;
import spring.myproject.dto.request.user.UserRequest;
import spring.myproject.dto.response.user.EmailCertificationResponse;
import spring.myproject.dto.response.user.IdCheckResponse;
import spring.myproject.dto.response.user.SignInResponse;
import spring.myproject.dto.response.user.SignUpResponse;
import spring.myproject.exception.user.NotFoundUserException;
import spring.myproject.exception.user.UnCorrectPasswordException;
import spring.myproject.provider.EmailProvider;
import spring.myproject.provider.JwtProvider;
import spring.myproject.s3.S3ImageUploadService;

import java.util.List;

import static spring.myproject.util.UserConst.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;
//    private final JwtProvider jwtProvider;
    private final EmailProvider emailProvider;
    private final S3ImageUploadService s3ImageUploadService;
    private final AuthenticationManager authenticationManager;

    @Value("${file.dir}")
    private String fileDir;

    public IdCheckResponse idCheck(IdCheckRequest idCheckRequest) {

        boolean idCheck = !userRepository.existsByUsername(idCheckRequest.getUsername());
        try {
            if (idCheck == true) {
                return IdCheckResponse.builder()
                        .code(successCode)
                        .message(successMessage)
                        .build();

            } else {
                return IdCheckResponse.builder()
                        .code(existCode)
                        .message(existMessage)
                        .build();
            }
        }catch (Exception e){
            log.error("error", e);
            return IdCheckResponse.builder()
                    .code(dbErrorCode)
                    .message(dbErrorMessage)
                    .build();

        }

    }

    public SignUpResponse signUp(UserRequest userRequest, MultipartFile file){




        try {


            Image image = null;
            String url = s3ImageUploadService.upload(file);

            if(StringUtils.hasText(url)){

                image = Image.builder()
                            .url(url)
                            .build();
                imageRepository.save(image);
            }

            User user = User.builder()
                    .age(userRequest.getAge())
                    .email(userRequest.getEmail())
                    .hobby(userRequest.getHobby())
                    .address(userRequest.getAddress())
                    .username(userRequest.getUsername())
                    .password(passwordEncoder.encode(userRequest.getPassword()))
                    .profileImage(image)
                    .build();

            userRepository.save(user);

            return SignUpResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();

        }catch (Exception e){
            log.error("error", e);
            return SignUpResponse.builder()
                    .code(dbErrorCode)
                    .message(dbErrorMessage)
                    .build();
        }




    }


    public SignInResponse signIn(SignInRequest signInRequest, HttpSession session) {


        try {

            User user = userRepository.findByUsername(signInRequest.getUsername()).orElseThrow(() -> new NotFoundUserException("not Found User"));

//            boolean matches = passwordEncoder.matches(signInRequest.getPassword(), user.getPassword());
//
//            checkPassword(matches);
//
//            String token = jwtProvider.create(user.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            return SignInResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();

        }catch (NotFoundUserException e){
            return SignInResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();

        }catch (UnCorrectPasswordException e){
            return SignInResponse.builder()
                    .code(unCorrectCode)
                    .message(unCorrectMessage)
                    .build();

        }catch (Exception e){
            log.error("error", e);
            return SignInResponse.builder()
                    .code(dbErrorCode)
                    .message(dbErrorMessage)
                    .build();
        }
    }


    public EmailCertificationResponse emailCertification(EmailCertificationRequest emailCertificationRequest) {


        try {
            List<User> users = userRepository.findByEmail(emailCertificationRequest.getEmail());
            if(users.size() == 0){
                return EmailCertificationResponse.builder()
                        .code(notEmailCode)
                        .message(notEmailMessage)
                        .build();
            } else if (users.size() == 1) {
                emailProvider.sendCertificationMail(emailCertificationRequest.getEmail(), certificationNumber());
                return EmailCertificationResponse.builder()
                        .code(successCode)
                        .message(successMessage)
                        .build();
            }else{
                return EmailCertificationResponse.builder()
                        .code(duplicateEmailCode)
                        .message(duplicateEmailMessage)
                        .build();
            }
        }catch (Exception e){
            log.error("error", e);
            return EmailCertificationResponse.builder()
                    .code(dbErrorCode)
                    .message(dbErrorMessage)
                    .build();
        }



    }

    private void checkPassword(boolean matches) {
        if(!matches){
            throw new UnCorrectPasswordException("doesn't match Password!");
        }
    }


    private String certificationNumber(){
        int number = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(number);
    }
}
