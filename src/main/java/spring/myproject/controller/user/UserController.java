package spring.myproject.controller.user;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.async.AsyncService;
import spring.myproject.service.fcm.FCMService;
import spring.myproject.service.user.UserService;

import static spring.myproject.dto.request.user.UserRequestDto.*;
import static spring.myproject.dto.response.user.UserResponseDto.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AsyncService asyncService;
    private final FCMService fcmService;

    @PostMapping("/auth/id-check")
    public ResponseEntity<IdCheckResponse> idCheck(@RequestBody IdCheckRequest idCheckRequest) {

        IdCheckResponse idCheckResponse = userService.idCheck(idCheckRequest);
        return new ResponseEntity<>(idCheckResponse, HttpStatus.OK);
    }
    @PostMapping("/auth/nickname-check")
    public ResponseEntity<NicknameCheckResponse> nicknameCheck(@RequestBody NicknameCheckRequest nicknameCheckRequest) {

        NicknameCheckResponse nicknameCheckResponse = userService.nicknameCheck(nicknameCheckRequest);
        return new ResponseEntity<>(nicknameCheckResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/auth/sign-up",consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<SignUpResponse> signUp(@RequestPart SignUpRequest signUpRequest
            , @RequestPart(required = false,name = "file") MultipartFile file){

        SignUpResponse signUpResponse = userService.signUp(signUpRequest, file);
        return new ResponseEntity<>(signUpResponse, HttpStatus.OK);
    }


    @PostMapping("/auth/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest signInRequest, HttpServletResponse response) {

        SignInResponse signInResponse = userService.signIn(signInRequest,response);
        fcmService.saveFCMToken(signInRequest);
        return new ResponseEntity<>(signInResponse,HttpStatus.OK);
    }

    @PostMapping(value = "/auth/email-certification")
    public ResponseEntity<EmailCertificationResponse> emailCertification(@RequestBody EmailCertificationRequest emailCertificationRequest){

        EmailCertificationResponse emailCertificationResponse = userService.emailCertification(emailCertificationRequest);
        asyncService.asyncTask(emailCertificationRequest);
        return new ResponseEntity<>(emailCertificationResponse,HttpStatus.OK);
    }

    @PostMapping(value = "/auth/generateToken")
    public ResponseEntity<GenerateTokenResponse> generateToken(@CookieValue(value = "refreshToken") String refreshToken,
                                                               HttpServletResponse response){
        GenerateTokenResponse generateTokenResponse = userService.generateToken(refreshToken,response);
        return new ResponseEntity<>(generateTokenResponse,HttpStatus.OK);
    }
}
