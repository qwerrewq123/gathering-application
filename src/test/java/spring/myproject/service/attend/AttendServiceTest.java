package spring.myproject.service.attend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.common.async.AsyncService;
import spring.myproject.common.exception.attend.NotFoundAttendException;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.common.exception.meeting.NotAuthorizeException;
import spring.myproject.dto.request.fcm.TopicNotificationRequestDto;
import spring.myproject.entity.attend.Attend;
import spring.myproject.entity.fcm.Topic;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.meeting.Meeting;
import spring.myproject.entity.user.Role;
import spring.myproject.entity.user.User;
import spring.myproject.common.exception.attend.AlreadyAttendExeption;
import spring.myproject.common.exception.meeting.NotFoundMeetingExeption;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.repository.attend.AttendRepository;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.repository.meeting.MeetingRepository;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.service.recommend.RecommendService;
import spring.myproject.utils.MockData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static spring.myproject.dto.response.attend.AttendResponseDto.*;
import static spring.myproject.utils.ConstClass.*;
import static spring.myproject.utils.MockData.*;

@ExtendWith(MockitoExtension.class)
public class AttendServiceTest {
    @InjectMocks
    AttendService attendService;
    @Mock
    UserRepository userRepository;
    @Mock
    GatheringRepository gatheringRepository;
    @Mock
    AttendRepository attendRepository;
    @Mock
    MeetingRepository meetingRepository;
    @Mock
    RecommendService recommendService;
    @Mock
    AsyncService asyncService;
    @DisplayName("Throws NotFoundUserException")
    @Test
    public void addAttendThrowsNotFoundUserException(){
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->attendService.addAttend(1L,1L,1L))
                .isInstanceOf(NotFoundUserException.class);
    }
    @DisplayName("Throws NotFoundGatheringException")
    @Test
    public void addAttendThrowsNotFoundGatheringException(){
        User mockUser = returnMockUser(1L,"true username","true password");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(gatheringRepository.findTopicById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->attendService.addAttend(1L,1L,1L))
                .isInstanceOf(NotFoundGatheringException.class);
    }
    @DisplayName("Throws NotFoundMeetingException")
    @Test
    public void addAttendThrowsNotFoundMetingException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(gatheringRepository.findTopicById(1L))
                .thenReturn(Optional.of(mockGathering));
        when(meetingRepository.findById(1L))
                .thenReturn(Optional.empty());
        assertThatThrownBy(()->attendService.addAttend(1L,1L,1L))
                .isInstanceOf(NotFoundMeetingExeption.class);
    }
    @DisplayName("Throws AlreadyAttendException")
    @Test
    public void addAttendThrowsAlreadyAttendException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser);
        Meeting mockMeeting = returnMockMeeting(mockGathering,mockUser);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(gatheringRepository.findTopicById(1L))
                .thenReturn(Optional.of(mockGathering));
        when(meetingRepository.findById(1L))
                .thenReturn(Optional.of(mockMeeting));
        when(attendRepository.findByUserIdAndMeetingId(1L,1L))
                .thenReturn(mock(Attend.class));
        assertThatThrownBy(()->attendService.addAttend(1L,1L,1L))
                .isInstanceOf(AlreadyAttendExeption.class);
    }
    @DisplayName("Return Normal Response")
    @Test
    public void addAttend() {
        User mockUser = returnMockUser(1L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser);
        Meeting mockMeeting = returnMockMeeting(mockGathering,mockUser);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(gatheringRepository.findTopicById(1L))
                .thenReturn(Optional.of(mockGathering));
        when(meetingRepository.findById(1L))
                .thenReturn(Optional.of(mockMeeting));
        when(attendRepository.findByUserIdAndMeetingId(1L,1L))
                .thenReturn(null);
        when(attendRepository.save(any(Attend.class)))
                .thenReturn(mock(Attend.class));
        doNothing().when(meetingRepository).updateCount(anyLong(),anyInt());
        doNothing().when(asyncService).sendTopic(any(TopicNotificationRequestDto.class));

        AddAttendResponse addAttendResponse = attendService.addAttend(1L, 1L,1L);

        assertThat(addAttendResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);
    }
    @DisplayName("Throws NotFoundUserException")
    @Test
    public void disAttendThrowsNotFoundUserException(){
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->attendService.disAttend(1L,1L,1L))
                .isInstanceOf(NotFoundUserException.class);
    }
    @DisplayName("Throws NotFoundMeetingException")
    @Test
    public void disAttendThrowsNotFoundMeetingException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(meetingRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->attendService.disAttend(1L,1L,1L))
                .isInstanceOf(NotFoundMeetingExeption.class);
    }
    @DisplayName("Throws NotFoundAttendException")
    @Test
    public void disAttendThrowsNotFoundAttendException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser);
        Meeting mockMeeting = returnMockMeeting(mockGathering,mockUser);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(meetingRepository.findById(1L))
                .thenReturn(Optional.of(mockMeeting));
        when(attendRepository.findByUserIdAndMeetingId(1L,1L))
                .thenReturn(null);

        assertThatThrownBy(()->attendService.disAttend(1L,1L,1L))
                .isInstanceOf(NotFoundAttendException.class);
    }
    @Test
    public void disAttend() {
        User mockUser1 = returnMockUser(1L,"true username","true password");
        User mockUser2 = returnMockUser(2L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser1);
        Meeting mockMeeting = returnMockMeeting(mockGathering,mockUser1);
        Attend mockAttend = returnMockAttend(mockMeeting,mockUser2);
        when(userRepository.findById(2L))
                .thenReturn(Optional.of(mockUser2));
        when(meetingRepository.findById(1L))
                .thenReturn(Optional.of(mockMeeting));
        when(attendRepository.findByUserIdAndMeetingId(2L,1L))
                .thenReturn(mockAttend);
        doNothing().when(attendRepository).delete(any(Attend.class));
        doNothing().when(meetingRepository).updateCount(anyLong(),anyInt());
        doNothing().when(recommendService).addScore(anyLong(),anyInt());

        DisAttendResponse disAttendResponse = attendService.disAttend(1L, 2L, 1L);

        assertThat(disAttendResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE,SUCCESS_MESSAGE);

    }
}
