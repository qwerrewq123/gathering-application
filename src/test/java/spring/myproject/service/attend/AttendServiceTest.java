package spring.myproject.service.attend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import spring.myproject.entity.attend.Attend;
import spring.myproject.entity.meeting.Meeting;
import spring.myproject.entity.user.Role;
import spring.myproject.entity.user.User;
import spring.myproject.common.exception.attend.AlreadyAttendExeption;
import spring.myproject.common.exception.attend.NotFoundAttendException;
import spring.myproject.common.exception.attend.NotWithdrawException;
import spring.myproject.common.exception.meeting.NotAuthorizeException;
import spring.myproject.common.exception.meeting.NotFoundMeetingExeption;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.repository.attend.AttendRepository;
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
    AttendRepository attendRepository;
    @MockitoBean
    MeetingRepository meetingRepository;
    @MockitoBean
    RecommendService recommendService;
    @Test
    public void addAttend() {
        User mockUser1 = new User(1L,"true username1","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        User mockUser2 = new User(2L,"true username2","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser2));
        when(userRepository.findById(3L)).thenReturn(Optional.empty());
        when(meetingRepository.findById(1L)).thenReturn(Optional.of(mock(Meeting.class)));
        when(meetingRepository.findById(2L)).thenReturn(Optional.empty());
        when(attendRepository.findByUserIdAndMeetingId(eq(1L),anyLong())).thenReturn(null);
        when(attendRepository.findByUserIdAndMeetingId(eq(2L),anyLong())).thenReturn(mock(Attend.class));
        assertThatThrownBy(()->attendService.addAttend(2L,3L,1L))
                .isInstanceOf(NotFoundUserException.class);
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
    public void permitAttend() {
        User mockUser1 = new User(1L,"true username1","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        User mockUser2 = new User(2L,"true username2","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);

        Meeting mockMeeting = Meeting.builder().createdBy(mockUser1).build();
        Attend mockAttend = Attend.builder().attendBy(mockUser1).accepted(false).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser2));
        when(userRepository.findById(3L)).thenReturn(Optional.empty());
        when(meetingRepository.findById(1L)).thenReturn(Optional.of(mockMeeting));
        when(meetingRepository.findById(2L)).thenReturn(Optional.empty());
        when(attendRepository.findByIdAndAccepted(eq(1L),anyBoolean())).thenReturn(Optional.of(mockAttend));
        when(attendRepository.findByIdAndAccepted(eq(2L),anyBoolean())).thenThrow(NotFoundAttendException.class);

        assertThatThrownBy(()->attendService.permitAttend(2L,3L,3L))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->attendService.permitAttend(2L,3L,2L))
                .isInstanceOf(NotFoundMeetingExeption.class);
        assertThatThrownBy(()->attendService.permitAttend(1L,2L,2L))
                .isInstanceOf(NotFoundAttendException.class);
        assertThatThrownBy(()->attendService.permitAttend(1L,1L,2L))
                .isInstanceOf(NotAuthorizeException.class);
        PermitAttendResponse permitAttendResponse = attendService.permitAttend(1L, 1L, 1L);
        assertThat(permitAttendResponse)
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
        Attend mockAttend = Attend.builder().attendBy(mockUser1).accepted(true).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser2));
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        when(meetingRepository.findById(1L)).thenReturn(Optional.of(mockMeeting));
        when(meetingRepository.findById(2L)).thenReturn(Optional.empty());

        when(attendRepository.findById(1L)).thenReturn(Optional.of(mockAttend));
        when(attendRepository.findById(2L)).thenReturn(Optional.empty());
        doNothing().when(meetingRepository).delete(any(Meeting.class));
        doNothing().when(recommendService).addScore(anyLong(),anyInt());
        assertThatThrownBy(()->attendService.disAttend(2L,2L,3L,1L))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->attendService.disAttend(2L,2L,2L,1L))
                .isInstanceOf(NotFoundMeetingExeption.class);
        assertThatThrownBy(()->attendService.disAttend(1L,2L,2L,1L))
                .isInstanceOf(NotFoundAttendException.class);
        assertThatThrownBy(()->attendService.disAttend(1L,1L,2L,1L))
                .isInstanceOf(NotAuthorizeException.class);
        DisAttendResponse disAttendResponse = attendService.disAttend(1L, 1L, 1L, 1L);
        assertThat(disAttendResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);
    }
}
