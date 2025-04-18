package spring.myproject.service.enrollment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import spring.myproject.entity.enrollment.Enrollment;
import spring.myproject.entity.fcm.Topic;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.user.Role;
import spring.myproject.entity.user.User;
import spring.myproject.common.exception.enrollment.AlreadyEnrollmentException;
import spring.myproject.common.exception.enrollment.NotFoundEnrollmentException;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.repository.enrollment.EnrollmentRepository;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.service.fcm.FCMService;
import spring.myproject.service.recommend.RecommendService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static spring.myproject.dto.response.enrollment.EnrollResponseDto.*;
import static spring.myproject.utils.ConstClass.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {

    @Autowired
    EnrollmentService enrollmentService;
    @MockitoBean
    EnrollmentRepository enrollmentRepository;
    @MockitoBean
    UserRepository userRepository;
    @MockitoBean
    GatheringRepository gatheringRepository;
    @MockitoBean
    FCMService fcmService;
    @MockitoBean
    RecommendService recommendService;

    @Test
    void enrollGathering(){
        User mockUser = new User(1L,"true username","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        Gathering mockGathering = new Gathering(1L,null,null,null,null,mockUser,0,null,null, Topic.builder().topicName("topicname").build());
        Gathering falseMockGathering = new Gathering(3L,null,null,null,null,mockUser,0,null,null,null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        when(gatheringRepository.findById(1L)).thenReturn(Optional.of(mockGathering));
        when(gatheringRepository.findById(2L)).thenReturn(Optional.empty());
        when(gatheringRepository.findById(3L)).thenReturn(Optional.of(falseMockGathering));
        when(enrollmentRepository.existEnrollment(eq(1L),anyLong())).thenReturn(null);
        when(enrollmentRepository.existEnrollment(eq(3L),anyLong())).thenReturn(mock(Enrollment.class));
        assertThatThrownBy(()->enrollmentService.enrollGathering(2L,2L))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->enrollmentService.enrollGathering(2L,1L))
                .isInstanceOf(NotFoundGatheringException.class);
        assertThatThrownBy(()->enrollmentService.enrollGathering(3L,1L))
                .isInstanceOf(AlreadyEnrollmentException.class);
        EnrollGatheringResponse enrollGatheringResponse = enrollmentService.enrollGathering(1L, 1L);
        assertThat(enrollGatheringResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);

    }
    @Test
    void disEnrollGathering(){
        User mockUser = new User(1L,"true username","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        Gathering mockGathering = new Gathering(1L,null,null,null,null,mockUser,0,null,null, Topic.builder().topicName("topicname").build());
        Gathering falseMockGathering = new Gathering(3L,null,null,null,null,mockUser,0,null,null,null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        when(gatheringRepository.findById(1L)).thenReturn(Optional.of(mockGathering));
        when(gatheringRepository.findById(2L)).thenReturn(Optional.empty());
        when(gatheringRepository.findById(3L)).thenReturn(Optional.of(falseMockGathering));
        when(enrollmentRepository.findEnrollment(eq(1L),anyLong())).thenReturn(Optional.of(mock(Enrollment.class)));
        when(enrollmentRepository.findEnrollment(eq(3L),anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(()->enrollmentService.disEnrollGathering(2L,2L))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->enrollmentService.disEnrollGathering(2L,1L))
                .isInstanceOf(NotFoundGatheringException.class);
        assertThatThrownBy(()->enrollmentService.disEnrollGathering(3L,1L))
                .isInstanceOf(NotFoundEnrollmentException.class);
        DisEnrollGatheringResponse disEnrollGatheringResponse = enrollmentService.disEnrollGathering(1L, 1L);
        assertThat(disEnrollGatheringResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

}
