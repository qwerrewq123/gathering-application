package spring.myproject.domain.user.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.async.AsyncService;
import spring.myproject.domain.user.dto.request.*;
import spring.myproject.domain.user.dto.response.*;
import spring.myproject.domain.user.service.UserService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AsyncService asyncService;

    @PostMapping("/auth/id-check")
    public ResponseEntity<IdCheckResponse> idCheck(@RequestBody IdCheckRequest idCheckRequest) {

        IdCheckResponse idCheckResponse = userService.idCheck(idCheckRequest);
        return new ResponseEntity<>(idCheckResponse, HttpStatus.OK);
    }
    @PostMapping("/auth/nickname-check")
    public ResponseEntity<IdCheckResponse> nicknameCheck(@RequestBody NicknameCheckRequest nicknameCheckRequest) {

        NicknameCheckResponse nicknameCheckResponse = userService.nicknameCheck(nicknameCheckRequest);
        return new ResponseEntity<>(nicknameCheckResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/auth/sign-up",consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<SignUpResponse> signUp(@RequestPart("userRequest") UserRequest userRequest, @RequestParam(required = false) MultipartFile file) throws IOException {

        SignUpResponse signUpResponse = userService.signUp(userRequest, file);
        return new ResponseEntity<>(signUpResponse, HttpStatus.OK);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserResponse> fetchUser(@PathVariable("userId") Long userId) {
        UserResponse userResponse = userService.fetchUser(userId);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);

    }

    @PostMapping("/auth/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest signInRequest, HttpSession session) {

        SignInResponse signInResponse = userService.signIn(signInRequest,session);
        return new ResponseEntity<>(signInResponse,HttpStatus.OK);
    }

    @PostMapping(value = "/auth/email-certification")
    public ResponseEntity<EmailCertificationResponse> emailCertification(@RequestBody EmailCertificationRequest emailCertificationRequest){

        EmailCertificationResponse emailCertificationResponse = userService.emailCertification(emailCertificationRequest);
        asyncService.asyncTask(emailCertificationRequest);
        return new ResponseEntity<>(emailCertificationResponse,HttpStatus.OK);
    }
}
