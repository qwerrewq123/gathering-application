package spring.myproject.service.enrollment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.common.exception.enrollment.NotDisEnrollmentException;
import spring.myproject.entity.alarm.Alarm;
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
import spring.myproject.service.alarm.AlarmService;
import spring.myproject.service.fcm.FCMService;
import spring.myproject.service.fcm.FCMTokenTopicService;
import spring.myproject.service.recommend.RecommendService;
import spring.myproject.utils.MockData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static spring.myproject.dto.response.enrollment.EnrollResponseDto.*;
import static spring.myproject.utils.ConstClass.*;
import static spring.myproject.utils.MockData.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {

    @InjectMocks
    EnrollmentService enrollmentService;
    @Mock
    EnrollmentRepository enrollmentRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    GatheringRepository gatheringRepository;
    @Mock
    RecommendService recommendService;
    @Mock
    FCMTokenTopicService fcmTokenTopicService;
    @Mock
    AlarmService alarmService;

    @DisplayName("Throw NotFoundUserException")
    @Test
    void enrollmentGatheringThrowsNotFoundUserException(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(()->enrollmentService.enrollGathering(1L,1L))
                .isInstanceOf(NotFoundUserException.class);
    }
    @DisplayName("Throw NotFoundGatheringException")
    @Test
    void enrollmentGatheringThrowsNotFoundGatheringException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(gatheringRepository.findGatheringFetchCreatedByAndTokensId(1L))
                .thenReturn(Optional.empty());
        assertThatThrownBy(()->enrollmentService.enrollGathering(1L,1L))
                .isInstanceOf(NotFoundGatheringException.class);
    }
    @DisplayName("Throw AlreadyEnrollmentException")
    @Test
    void enrollmentGatheringThrowsAlreadyEnrollmentException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser);
        Enrollment mockEnrollment = returnMockEnrollment(mockGathering,mockUser);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(gatheringRepository.findGatheringFetchCreatedByAndTokensId(1L))
                .thenReturn(Optional.of(mockGathering));
        when(enrollmentRepository.existEnrollment(1L,1L))
                .thenReturn(mockEnrollment);

        assertThatThrownBy(()->enrollmentService.enrollGathering(1L,1L))
                .isInstanceOf(AlreadyEnrollmentException.class);
    }
    @DisplayName("Return 200 Response")
    @Test
    void enrollGathering(){
        User mockUser = returnMockUser(1L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser);
        Enrollment mockEnrollment = returnMockEnrollment(mockGathering,mockUser);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(gatheringRepository.findGatheringFetchCreatedByAndTokensId(1L))
                .thenReturn(Optional.of(mockGathering));
        when(enrollmentRepository.existEnrollment(1L,1L)).thenReturn(null);
        when(enrollmentRepository.save(any(Enrollment.class)))
                .thenReturn(mock(Enrollment.class));
        doNothing().when(recommendService).addScore(1L,1);
        doNothing().when(fcmTokenTopicService).sendByToken(any(), any());
        doNothing().when(alarmService).save(any(Alarm.class));
        EnrollGatheringResponse enrollGatheringResponse = enrollmentService.enrollGathering(1L, 1L);

        assertThat(enrollGatheringResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);

    }

    @DisplayName("Throw NotFoundUserException")
    @Test
    void disEnrollmentGatheringThrowsNotFoundUserException(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(()->enrollmentService.enrollGathering(1L,1L))
                .isInstanceOf(NotFoundUserException.class);
    }
    @DisplayName("Throw NotFoundGatheringException")
    @Test
    void disEnrollmentGatheringThrowsNotFoundGatheringException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(gatheringRepository.findGatheringFetchCreatedByAndTokensId(1L))
                .thenReturn(Optional.empty());
        assertThatThrownBy(()->enrollmentService.enrollGathering(1L,1L))
                .isInstanceOf(NotFoundGatheringException.class);
    }
    @DisplayName("Throw NotFoundEnrollmentException")
    @Test
    void disEnrollmentGatheringThrowsAlreadyEnrollmentException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(gatheringRepository.findGatheringFetchCreatedAndTopicBy(1L))
                .thenReturn(Optional.of(mockGathering));
        when(enrollmentRepository.findEnrollment(1L,1L,true))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->enrollmentService.disEnrollGathering(1L,1L))
                .isInstanceOf(NotFoundEnrollmentException.class);
    }
    @DisplayName("Throw NotDisEnrollmentException")
    @Test
    void disEnrollmentGatheringThrowsNotDisEnrollmentException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser);
        Enrollment mockEnrollment = returnMockEnrollment(mockGathering,mockUser);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(gatheringRepository.findGatheringFetchCreatedAndTopicBy(1L))
                .thenReturn(Optional.of(mockGathering));
        when(enrollmentRepository.findEnrollment(1L,1L,true))
                .thenReturn(Optional.of(mockEnrollment));

        assertThatThrownBy(()->enrollmentService.disEnrollGathering(1L,1L))
                .isInstanceOf(NotDisEnrollmentException.class);
    }
    @DisplayName("Return 200 Response")
    @Test
    void disEnrollGathering(){
        User mockUser1 = returnMockUser(1L,"true username","true password");
        User mockUser2 = returnMockUser(2L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser1);
        Enrollment mockEnrollment = returnMockEnrollment(mockGathering,mockUser2);
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser2));
        when(gatheringRepository.findGatheringFetchCreatedAndTopicBy(1L))
                .thenReturn(Optional.of(mockGathering));
        when(enrollmentRepository.findEnrollment(1L,2L,true))
                .thenReturn(Optional.of(mockEnrollment));
        doNothing().when(enrollmentRepository).delete(any(Enrollment.class));
        doNothing().when(gatheringRepository).updateCount(1L,-1);
        doNothing().when(fcmTokenTopicService).unsubscribeFromTopic(any(String.class),eq(2L));
        doNothing().when(recommendService).addScore(1L,-1);

        DisEnrollGatheringResponse disEnrollGatheringResponse = enrollmentService.disEnrollGathering(1L, 2L);

        assertThat(disEnrollGatheringResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

}
