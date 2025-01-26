package spring.myproject.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.domain.user.service.UserService;
import spring.myproject.dto.request.user.EmailCertificationRequest;
import spring.myproject.dto.request.user.IdCheckRequest;
import spring.myproject.dto.request.user.SignInRequest;
import spring.myproject.dto.request.user.UserRequest;
import spring.myproject.dto.response.user.EmailCertificationResponse;
import spring.myproject.dto.response.user.IdCheckResponse;
import spring.myproject.dto.response.user.SignInResponse;
import spring.myproject.dto.response.user.SignUpResponse;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/id-check")
    public ResponseEntity<Object> idCheck(@RequestBody IdCheckRequest idCheckRequest) {
        try {
            Boolean idCheck = !userService.idCheck(idCheckRequest);
            if (idCheck == true) {
                IdCheckResponse idCheckResponse = IdCheckResponse.builder()
                        .code("SU")
                        .message("Success")
                        .build();
                return new ResponseEntity<>(idCheckResponse, HttpStatus.OK);
            } else {
                IdCheckResponse idCheckResponse = IdCheckResponse.builder()
                        .code("UE")
                        .message("User Exist")
                        .build();
                return new ResponseEntity<>(idCheckResponse, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/auth/sign-up")
    public ResponseEntity<Object> signUp(@RequestBody UserRequest userRequest, @RequestParam MultipartFile file) throws IOException {
        try {
            userService.signUp(userRequest, file);
            SignUpResponse signUpResponse = SignUpResponse.builder()
                    .code("SU")
                    .message("Success")
                    .build();
            return new ResponseEntity<>(signUpResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/auth/sign-in")
    public ResponseEntity<Object> signIn(@RequestBody SignInRequest signInRequest) {

        try {

            String token = userService.signIn(signInRequest);
            SignInResponse signInResponse = SignInResponse.builder()
                    .code("SU")
                    .message("Success")
                    .token(token)
                    .build();
            return new ResponseEntity<>(signInResponse,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }

    @PostMapping("/auth/email-certification")
    public ResponseEntity<Object> emailCertification(@RequestBody EmailCertificationRequest emailCertificationRequest){
        try {
            userService.emailCertification(emailCertificationRequest);

            EmailCertificationResponse emailCertificationResponse = EmailCertificationResponse.builder()
                    .code("SU")
                    .message("Success")

                    .build();
            return new ResponseEntity<>(emailCertificationResponse,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }


}
