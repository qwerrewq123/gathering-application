package spring.myproject.service.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.common.async.AsyncService;
import spring.myproject.common.exception.user.*;
import spring.myproject.entity.fcm.FCMToken;
import spring.myproject.entity.image.Image;
import spring.myproject.entity.user.Role;
import spring.myproject.repository.certification.CertificationRepository;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.common.provider.JwtProvider;
import spring.myproject.common.s3.S3ImageUploadService;
import spring.myproject.service.fcm.FCMTokenTopicService;
import spring.myproject.utils.MockData;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static spring.myproject.dto.request.user.UserRequestDto.*;
import static spring.myproject.dto.response.user.UserResponseDto.*;
import static spring.myproject.utils.ConstClass.*;
import static spring.myproject.utils.MockData.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;
    @Mock
    CertificationRepository certificationRepository;
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

    @DisplayName("Throws ExistUserException")
    @Test
    void idCheckThrowsExistUserException(){
        when(userRepository.existsByUsername("false username")).thenReturn(true);
        IdCheckRequest idCheckRequest = IdCheckRequest.builder()
                .username("false username")
                .build();

        assertThatThrownBy(()->userService.idCheck(idCheckRequest))
                .isInstanceOf(ExistUserException.class);
    }

    @DisplayName("Return Normal Response")
    @Test
    void idCheck() {
        when(userRepository.existsByUsername("true username")).thenReturn(false);
        IdCheckRequest idCheckRequest = IdCheckRequest.builder()
                .username("true username")
                .build();

        IdCheckResponse idCheckResponse = userService.idCheck(idCheckRequest);

        assertThat(idCheckResponse).isInstanceOf(IdCheckResponse.class)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);
    }
    @DisplayName("Throws UserExistException")
    @Test
    void nicknameCheckThrowsExistUserException(){
        when(userRepository.existsByNickname("false nickname")).thenReturn(true);
        NicknameCheckRequest nicknameCheckRequest = NicknameCheckRequest.builder()
                .nickname("false nickname")
                .build();

        assertThatThrownBy(()->userService.nicknameCheck(nicknameCheckRequest))
                .isInstanceOf(ExistUserException.class);

    }
    @DisplayName("Return Normal Response")
    @Test
    void nicknameCheck() {
        when(userRepository.existsByNickname("true nickname")).thenReturn(false);
        NicknameCheckRequest nicknameCheckRequest = NicknameCheckRequest.builder()
                .nickname("true nickname")
                .build();

        NicknameCheckResponse nicknameCheckResponse = userService.nicknameCheck(nicknameCheckRequest);

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
    @DisplayName("Throws NotFoundUserException")
    @Test
    void signInThrowsNotFoundUserException(){
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(userRepository.findByUsername("false username")).thenThrow(NotFoundUserException.class);
        SignInRequest signInRequest = SignInRequest.builder()
                .username("false username")
                .password("false password")
                .build();
        assertThatThrownBy(()->userService.signIn(signInRequest,mockResponse))
                .isInstanceOf(NotFoundUserException.class);
    }
    @DisplayName("Throws UnCorrectPasswordException")
    @Test
    void signInThrowsUnCorrectPasswordException(){
        User mockUser = returnMockUser(1L,"true username","false password");
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(userRepository.findByUsername("true username")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(eq("false password"),any(String.class))).thenReturn(false);
        SignInRequest signInRequest = SignInRequest.builder()
                .username("true username")
                .password("false password")
                .build();
        assertThatThrownBy(()->userService.signIn(signInRequest,mockResponse))
                .isInstanceOf(UnCorrectPasswordException.class);

    }
    @DisplayName("return normal Response")
    @Test
    void signIn() {
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        User mockUser = returnMockUser(1L,"true username","true password");
        String accessToken = returnAccessToken();
        String refreshToken = returnRefreshToken();
        when(userRepository.findByUsername("true username")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(eq("true password"),any(String.class))).thenReturn(true);
        when(jwtProvider.createAccessToken(mockUser)).thenReturn(accessToken);
        when(jwtProvider.createRefreshToken(mockUser)).thenReturn(refreshToken);
        doNothing().when(mockResponse).addCookie(any(Cookie.class));

        SignInRequest signInRequest = SignInRequest.builder()
                .username("true username")
                .password("true password")
                .build();

        SignInResponse signInResponse = userService.signIn(signInRequest, mockResponse);

        assertThat(signInResponse).isInstanceOf(SignInResponse.class)
                .extracting("code","message","accessToken")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE,"accessToken");
    }
    @DisplayName("Throws DuplicateEmailException")
    @Test
    void emailCertificationThrowsDuplicateEmailException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        when(userRepository.findByEmail("false email")).thenReturn(List.of(mockUser));
        EmailCertificationRequest emailCertificationRequest = EmailCertificationRequest.builder()
                .email("false email")
                .build();

        assertThatThrownBy(()->userService.emailCertification(emailCertificationRequest))
                .isInstanceOf(DuplicateEmailExeption.class);

    }
    @Test
    void emailCertification() {
        when(userRepository.findByEmail("true email")).thenReturn(List.of());
        doNothing().when(asyncService).asyncTask(any(EmailCertificationRequest.class));
        EmailCertificationRequest emailCertificationRequest = EmailCertificationRequest.builder()
                .email("true email")
                .build();

        EmailCertificationResponse emailCertificationResponse = userService.emailCertification(emailCertificationRequest);

        assertThat(emailCertificationResponse).isInstanceOf(EmailCertificationResponse.class)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);

    }
    @DisplayName("Throws NotFoundCertificationException")
    @Test
    void checkCertificationThrowsNotFoundCertificationException() {
        when(certificationRepository.findCertificationByEmail("false Email")).thenReturn(null);
        CheckCertificationRequest checkCertificationRequest = CheckCertificationRequest.builder()
                .email("false Email")
                .build();
        assertThatThrownBy(()->userService.checkCertification(checkCertificationRequest))
                .isInstanceOf(NotFoundCertificationException.class);
    }
    @DisplayName("Throws UnCorrectCertificationException")
    @Test
    void checkCertificationThrowsUnCorrectCertificationException() {
        when(certificationRepository.findCertificationByEmail("true Email")).thenReturn("true certification");
        CheckCertificationRequest checkCertificationRequest = CheckCertificationRequest.builder()
                .certification("false certification")
                .email("true Email")
                .build();
        assertThatThrownBy(()->userService.checkCertification(checkCertificationRequest))
                .isInstanceOf(UnCorrectCertification.class);
    }
    @DisplayName("Return Normal Response")
    @Test
    void checkCertification() {
        when(certificationRepository.findCertificationByEmail("true Email")).thenReturn("true certification");
        CheckCertificationRequest checkCertificationRequest = CheckCertificationRequest.builder()
                .certification("true certification")
                .email("true Email")
                .build();

        CheckCertificationResponse checkCertificationResponse = userService.checkCertification(checkCertificationRequest);

        assertThat(checkCertificationResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
    @DisplayName("Throw NotFoundUserException")
    @Test
    void fetchUserThrowsNotFoundUserException() {
        when(userRepository.findByIdFetchImage(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(()->userService.fetchUser(2L))
                .isInstanceOf(NotFoundUserException.class);
    }
    @DisplayName("Return Normal Response")
    @Test
    void fetchUser() {
        User mockUser = returnMockUser(1L,"true username","true password");
        when(userRepository.findByIdFetchImage(1L)).thenReturn(Optional.of(mockUser));

        UserResponse userResponse = userService.fetchUser(1L);

        assertThat(userResponse)
                .extracting("code","message")
                    .containsExactly(SUCCESS_CODE,SUCCESS_MESSAGE);


    }
}