package spring.myproject.service.gathering;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.entity.category.Category;
import spring.myproject.entity.fcm.Topic;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.user.Role;
import spring.myproject.entity.user.User;
import spring.myproject.common.exception.category.NotFoundCategoryException;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.common.exception.meeting.NotAuthorizeException;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.repository.category.CategoryRepository;
import spring.myproject.repository.enrollment.EnrollmentRepository;
import spring.myproject.repository.fcm.TopicRepository;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.repository.recommend.RecommendRepository;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.common.s3.S3ImageUploadService;
import spring.myproject.service.fcm.FCMService;
import spring.myproject.service.recommend.RecommendService;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static spring.myproject.dto.request.gathering.GatheringRequestDto.*;
import static spring.myproject.dto.response.gathering.GatheringResponseDto.*;
import static spring.myproject.utils.ConstClass.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class GatheringServiceTest {
    @Autowired
    GatheringService gatheringService;
    @MockitoBean
    GatheringRepository gatheringRepository;
    @MockitoBean
    EnrollmentRepository enrollmentRepository;
    @MockitoBean
    ImageRepository imageRepository;
    @MockitoBean
    CategoryRepository categoryRepository;
    @MockitoBean
    UserRepository userRepository;
    @MockitoBean
    S3ImageUploadService s3ImageUploadService;
    @MockitoBean
    FCMService fcmService;
    @MockitoBean
    TopicRepository topicRepository;
    @MockitoBean
    RecommendService recommendService;
    @Value("${server.url}")
    private String url;

    @Test
    void addGathering() throws IOException {
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        User mockUser = new User(1L,"true username","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        when(categoryRepository.findByName("true category")).thenReturn(Optional.of(mock(Category.class)));
        when(categoryRepository.findByName("false category")).thenReturn(Optional.empty());
        when(s3ImageUploadService.upload(any(MultipartFile.class))).thenReturn("url");
        when(topicRepository.save(any(Topic.class))).thenReturn(mock(Topic.class));
        doNothing().when(recommendService).createScore(any(Gathering.class));
        AddGatheringRequest trueAddGatheringRequest = AddGatheringRequest.builder()
                .category("true category")
                .build();
        AddGatheringRequest falseAddGatheringRequest = AddGatheringRequest.builder()
                .category("false category")
                .build();
        assertThatThrownBy(()->gatheringService.addGathering(falseAddGatheringRequest,mockMultipartFile,2L))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->gatheringService.addGathering(falseAddGatheringRequest,mockMultipartFile,1L))
                .isInstanceOf(NotFoundCategoryException.class);
        AddGatheringResponse addGatheringResponse = gatheringService.addGathering(trueAddGatheringRequest, mockMultipartFile, 1L);
        assertThat(addGatheringResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);

    }
    @Test
    void updateGathering() throws IOException {
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        User mockUser = new User(1L,"true username","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        Gathering mockGathering = new Gathering(1L,null,null,null,null,mockUser,0,null,null,null);
        Gathering falseMockGathering = new Gathering(1L,null,null,null,null,User.builder().id(2L).build(),0,null,null,null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        when(categoryRepository.findByName("true category")).thenReturn(Optional.of(mock(Category.class)));
        when(categoryRepository.findByName("false category")).thenReturn(Optional.empty());
        when(gatheringRepository.findById(1L)).thenReturn(Optional.of(mockGathering));
        when(gatheringRepository.findById(2L)).thenReturn(Optional.empty());
        when(gatheringRepository.findById(3L)).thenReturn(Optional.of(falseMockGathering));
        when(s3ImageUploadService.upload(any(MultipartFile.class))).thenReturn("url");
        UpdateGatheringRequest trueUpdateGatheringRequest = UpdateGatheringRequest.builder()
                .category("true category")
                .build();
        UpdateGatheringRequest falseUpdateGatheringRequest = UpdateGatheringRequest.builder()
                .category("false category")
                .build();
        assertThatThrownBy(()->gatheringService.updateGathering(falseUpdateGatheringRequest,mockMultipartFile,2L,2L))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->gatheringService.updateGathering(falseUpdateGatheringRequest,mockMultipartFile,1L,2L))
                .isInstanceOf(NotFoundCategoryException.class);
        assertThatThrownBy(()->gatheringService.updateGathering(trueUpdateGatheringRequest,mockMultipartFile,1L,2L))
                .isInstanceOf(NotFoundGatheringException.class);
        assertThatThrownBy(()->gatheringService.updateGathering(trueUpdateGatheringRequest,mockMultipartFile,1L,3L))
                .isInstanceOf(NotAuthorizeException.class);
        UpdateGatheringResponse updateGatheringResponse = gatheringService.updateGathering(trueUpdateGatheringRequest, mockMultipartFile, 1L, 1L);
        assertThat(updateGatheringResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

}
