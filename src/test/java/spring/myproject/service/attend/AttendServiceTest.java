package spring.myproject.service.attend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import spring.myproject.common.async.AsyncService;
import spring.myproject.common.exception.attend.NotFoundAttendException;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static spring.myproject.dto.response.attend.AttendResponseDto.*;
import static spring.myproject.utils.ConstClass.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AttendServiceTest {
    @Autowired
    AttendService attendService;
    @MockitoBean
    UserRepository userRepository;
    @MockitoBean
    GatheringRepository gatheringRepository;
    @MockitoBean
    AttendRepository attendRepository;
    @MockitoBean
    MeetingRepository meetingRepository;
    @MockitoBean
    RecommendService recommendService;
    @MockitoBean
    AsyncService asyncService;
    @Test
    public void addAttend() {
        User mockUser1 = new User(1L,"true username1","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        User mockUser2 = new User(2L,"true username2","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        Gathering mockGathering = new Gathering(1L,null,null,null,null,null,0,null,null, Topic.builder().build());
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser2));
        when(userRepository.findById(3L)).thenReturn(Optional.empty());
        when(gatheringRepository.findTopicById(1L)).thenReturn(Optional.of(mockGathering));
        when(gatheringRepository.findTopicById(2L)).thenReturn(Optional.empty());
        when(meetingRepository.findById(1L)).thenReturn(Optional.of(mock(Meeting.class)));
        when(meetingRepository.findById(2L)).thenReturn(Optional.empty());
        when(attendRepository.findByUserIdAndMeetingId(eq(1L),anyLong())).thenReturn(null);
        when(attendRepository.findByUserIdAndMeetingId(eq(2L),anyLong())).thenReturn(mock(Attend.class));
        doNothing().when(asyncService).sendTopic(any(TopicNotificationRequestDto.class));
        assertThatThrownBy(()->attendService.addAttend(2L,3L,2L))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->attendService.addAttend(2L,2L,2L))
                .isInstanceOf(NotFoundGatheringException.class);
        assertThatThrownBy(()->attendService.addAttend(2L,2L,1L))
                .isInstanceOf(NotFoundMeetingExeption.class);
        assertThatThrownBy(()->attendService.addAttend(1L,2L,1L))
                .isInstanceOf(AlreadyAttendExeption.class);
        AddAttendResponse addAttendResponse = attendService.addAttend(1L, 1L,1L);
        assertThat(addAttendResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);
    }
    @Test
    public void disAttend() {
        User mockUser1 = new User(1L,"true username1","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        User mockUser2 = new User(2L,"true username2","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        Meeting mockMeeting = Meeting.builder().createdBy(mockUser1).count(1).build();
        Attend mockAttend = Attend.builder().attendBy(mockUser1).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser2));
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        when(meetingRepository.findById(1L)).thenReturn(Optional.of(mockMeeting));
        when(meetingRepository.findById(2L)).thenReturn(Optional.empty());

        when(attendRepository.findById(1L)).thenReturn(Optional.of(mockAttend));
        when(attendRepository.findById(2L)).thenReturn(Optional.empty());
        doNothing().when(meetingRepository).delete(any(Meeting.class));
        doNothing().when(recommendService).addScore(anyLong(),anyInt());
        assertThatThrownBy(()->attendService.disAttend(2L,3L,1L))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->attendService.disAttend(2L,2L,1L))
                .isInstanceOf(NotFoundMeetingExeption.class);
        assertThatThrownBy(()->attendService.disAttend(1L,2L,1L))
                .isInstanceOf(NotFoundAttendException.class);

    }
}
