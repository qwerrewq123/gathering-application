package spring.myproject.service.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.common.async.AsyncService;
import spring.myproject.common.exception.user.*;
import spring.myproject.entity.fcm.FCMToken;
import spring.myproject.entity.image.Image;
import spring.myproject.entity.user.Role;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.common.provider.JwtProvider;
import spring.myproject.common.s3.S3ImageUploadService;
import spring.myproject.service.fcm.FCMTokenTopicService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static spring.myproject.dto.request.user.UserRequestDto.*;
import static spring.myproject.dto.response.user.UserResponseDto.*;
import static spring.myproject.utils.ConstClass.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtProvider jwtProvider;
    @Mock
    S3ImageUploadService s3ImageUploadService;
    @Mock
    ImageRepository imageRepository;
    @Mock
    AsyncService asyncService;


    @Test
    void idCheck() {
        when(userRepository.existsByUsername("true username")).thenReturn(false);
        when(userRepository.existsByUsername("false username")).thenReturn(true);
        IdCheckRequest trueIdCheckRequest = IdCheckRequest.builder()
                .username("true username")
                .build();
        IdCheckRequest falseIdCheckRequest = IdCheckRequest.builder()
                .username("false username")
                .build();
        assertThatThrownBy(()->userService.idCheck(falseIdCheckRequest))
                .isInstanceOf(ExistUserException.class);
        IdCheckResponse idCheckResponse = userService.idCheck(trueIdCheckRequest);
        assertThat(idCheckResponse).isInstanceOf(IdCheckResponse.class)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);

    }

    @Test
    void nicknameCheck() {
        when(userRepository.existsByNickname("true nickname")).thenReturn(false);
        when(userRepository.existsByNickname("false nickname")).thenReturn(true);
        NicknameCheckRequest trueNicknameCheckRequest = NicknameCheckRequest.builder()
                .nickname("true nickname")
                .build();
        NicknameCheckRequest falseNicknameCheckRequest = NicknameCheckRequest.builder()
                .nickname("false nickname")
                .build();

        assertThatThrownBy(()->userService.nicknameCheck(falseNicknameCheckRequest))
                .isInstanceOf(ExistUserException.class);
        NicknameCheckResponse nicknameCheckResponse = userService.nicknameCheck(trueNicknameCheckRequest);
        assertThat(nicknameCheckResponse).isInstanceOf(NicknameCheckResponse.class)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    @Test
    void signUp() throws IOException {
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(s3ImageUploadService.upload(any(MultipartFile.class))).thenReturn("url");
        when(imageRepository.save(any(Image.class))).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encode password");
        when(userRepository.save(any(User.class))).thenReturn(null);

        SignUpRequest userRequest = new SignUpRequest("username", "password", "email", "address", 1, "hobby", "nickname");
        SignUpResponse signUpResponse = userService.signUp(userRequest, mockMultipartFile);
        assertThat(signUpResponse).isInstanceOf(SignUpResponse.class)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);

    }

    @Test
    void signIn() {
        User mockUser = new User(1L,"true username","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        when(userRepository.findByUsername("true username")).thenReturn(Optional.of(mockUser));
        when(userRepository.findByUsername("false username")).thenThrow(NotFoundUserException.class);
        when(passwordEncoder.matches(eq("true password"),any(String.class))).thenReturn(true);
        when(passwordEncoder.matches(eq("false password"),any(String.class))).thenReturn(false);
        when(jwtProvider.createAccessToken(mockUser)).thenReturn("accessToken");
        when(jwtProvider.createRefreshToken(mockUser)).thenReturn("refreshToken");
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        doNothing().when(mockResponse).addCookie(any(Cookie.class));

        SignInRequest trueSignRequest = SignInRequest.builder()
                .username("true username")
                .password("true password")
                .build();
        SignInRequest falseSignRequest1 = SignInRequest.builder()
                .username("false username")
                .password("false password")
                .build();
        SignInRequest falseSignRequest2 = SignInRequest.builder()
                .username("true username")
                .password("false password")
                .build();

        assertThatThrownBy(()->userService.signIn(falseSignRequest1,mockResponse))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->userService.signIn(falseSignRequest2,mockResponse))
                .isInstanceOf(UnCorrectPasswordException.class);
        SignInResponse signInResponse = userService.signIn(trueSignRequest, mockResponse);
        assertThat(signInResponse).isInstanceOf(SignInResponse.class)
                .extracting("code","message","accessToken")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE,"accessToken");
    }

    @Test
    void emailCertification() {
        when(userRepository.findByEmail("true email")).thenReturn(List.of());
        when(userRepository.findByEmail("false email")).thenReturn(List.of(mock(User.class), mock(User.class)));
        doNothing().when(asyncService).asyncTask(any());
        EmailCertificationRequest trueEmailCertificationRequest = EmailCertificationRequest.builder()
                .email("true email")
                .build();
        EmailCertificationRequest falseEmailCertificationRequest = EmailCertificationRequest.builder()
                .email("false email")
                .build();


        assertThatThrownBy(()->userService.emailCertification(falseEmailCertificationRequest))
                .isInstanceOf(DuplicateEmailExeption.class);
        EmailCertificationResponse emailCertificationResponse = userService.emailCertification(trueEmailCertificationRequest);
        assertThat(emailCertificationResponse).isInstanceOf(EmailCertificationResponse.class)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);


    }



}