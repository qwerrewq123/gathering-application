package spring.myproject.domain.user.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.async.AsyncService;
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
    private final AsyncService asyncService;

    @PostMapping("/auth/id-check")
    public ResponseEntity<IdCheckResponse> idCheck(@RequestBody IdCheckRequest idCheckRequest) {

        IdCheckResponse idCheckResponse = userService.idCheck(idCheckRequest);

        if (idCheckResponse.getCode().equals("SU")) {

            return new ResponseEntity<>(idCheckResponse, HttpStatus.OK);
        } else {

            return new ResponseEntity<>(idCheckResponse, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = "/auth/sign-up",consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<SignUpResponse> signUp(@RequestPart("userRequest") UserRequest userRequest, @RequestParam(required = false) MultipartFile file){

        SignUpResponse signUpResponse = userService.signUp(userRequest, file);
        if(signUpResponse.getCode().equals("SU")){
            return new ResponseEntity<>(signUpResponse, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(signUpResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auth/sign-in")
    public ResponseEntity<Object> signIn(@RequestBody SignInRequest signInRequest, HttpSession session) {

        SignInResponse signInResponse = userService.signIn(signInRequest,session);
        if(signInResponse.getCode().equals("SU")){
            return new ResponseEntity<>(signInResponse,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(signInResponse,HttpStatus.BAD_REQUEST);

        }


    }

    @PostMapping(value = "/auth/email-certification")
    public ResponseEntity<Object> emailCertification(@RequestBody EmailCertificationRequest emailCertificationRequest){

        EmailCertificationResponse emailCertificationResponse = userService.emailCertification(emailCertificationRequest);
        if(emailCertificationResponse.getCode().equals("SU")){
            asyncService.asyncTask(emailCertificationRequest);
            return new ResponseEntity<>(emailCertificationResponse,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(emailCertificationResponse,HttpStatus.BAD_REQUEST);
        }
    }


}
