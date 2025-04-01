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
import spring.myproject.exception.attend.AlreadyAttendExeption;
import spring.myproject.exception.attend.NotFoundAttendException;
import spring.myproject.exception.attend.NotWithdrawException;
import spring.myproject.exception.meeting.NotAuthorizeException;
import spring.myproject.exception.meeting.NotFoundMeetingExeption;
import spring.myproject.exception.user.NotFoundUserException;
import spring.myproject.repository.attend.AttendRepository;
import spring.myproject.repository.meeting.MeetingRepository;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.service.meeting.MeetingService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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
    @Autowired
    private MeetingService meetingService;

    @Test
    public void addAttend() {
        User mockUser1 = new User(1L,"true username1","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        User mockUser2 = new User(2L,"true username2","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        when(userRepository.findByUsername("true username1")).thenReturn(Optional.of(mockUser1));
        when(userRepository.findByUsername("true username2")).thenReturn(Optional.of(mockUser2));
        when(userRepository.findByUsername("false username")).thenReturn(Optional.empty());
        when(meetingRepository.findById(1L)).thenReturn(Optional.of(mock(Meeting.class)));
        when(meetingRepository.findById(2L)).thenReturn(Optional.empty());
        when(attendRepository.findByUserIdAndMeetingId(eq(1L),anyLong())).thenReturn(null);
        when(attendRepository.findByUserIdAndMeetingId(eq(2L),anyLong())).thenReturn(mock(Attend.class));
        assertThatThrownBy(()->attendService.addAttend(2L,"false username"))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->attendService.addAttend(2L,"true username2"))
                .isInstanceOf(NotFoundMeetingExeption.class);
        assertThatThrownBy(()->attendService.addAttend(1L,"true username2"))
                .isInstanceOf(AlreadyAttendExeption.class);
        AddAttendResponse addAttendResponse = attendService.addAttend(1L, "true username1");
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
        Attend mockAttend = Attend.builder().accepted(false).build();
        Attend falseMockAttend = Attend.builder().accepted(true).build();
        when(userRepository.findByUsername("true username1")).thenReturn(Optional.of(mockUser1));
        when(userRepository.findByUsername("true username2")).thenReturn(Optional.of(mockUser2));
        when(userRepository.findByUsername("false username")).thenReturn(Optional.empty());
        when(meetingRepository.findById(1L)).thenReturn(Optional.of(mockMeeting));
        when(meetingRepository.findById(2L)).thenReturn(Optional.empty());
        when(attendRepository.findById(1L)).thenReturn(Optional.of(mockAttend));
        when(attendRepository.findById(2L)).thenReturn(Optional.of(falseMockAttend));
        when(attendRepository.findById(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(()->attendService.permitAttend(2L,3L,"false username"))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->attendService.permitAttend(2L,3L,"true username2"))
                .isInstanceOf(NotFoundMeetingExeption.class);
        assertThatThrownBy(()->attendService.permitAttend(1L,3L,"true username2"))
                .isInstanceOf(NotFoundAttendException.class);
        assertThatThrownBy(()->attendService.permitAttend(1L,2L,"true username2"))
                .isInstanceOf(NotAuthorizeException.class);
        assertThatThrownBy(()->attendService.permitAttend(1L,2L,"true username1"))
                .isInstanceOf(AlreadyAttendExeption.class);
        PermitAttendResponse permitAttendResponse = attendService.permitAttend(1L, 1L, "true username1");
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
        Meeting mockMeeting1 = Meeting.builder().createdBy(mockUser1).count(1).build();
        Meeting mockMeeting2 = Meeting.builder().createdBy(mockUser1).count(10).build();
        Meeting falseMockMeeting = Meeting.builder().createdBy(mockUser1).count(3).build();
        when(userRepository.findByUsername("true username1")).thenReturn(Optional.of(mockUser1));
        when(userRepository.findByUsername("true username2")).thenReturn(Optional.of(mockUser2));
        when(userRepository.findByUsername("false username")).thenReturn(Optional.empty());
        when(meetingRepository.findById(1L)).thenReturn(Optional.of(mockMeeting1));
        when(meetingRepository.findById(3L)).thenReturn(Optional.of(mockMeeting2));
        when(meetingRepository.findById(2L)).thenReturn(Optional.of(falseMockMeeting));
        when(meetingRepository.findById(10L)).thenReturn(Optional.empty());
        when(attendRepository.findByIdAndAccepted(1L,true)).thenReturn(Optional.of(mock(Attend.class)));
        when(attendRepository.findByIdAndAccepted(2L,true)).thenReturn(Optional.empty());

        assertThatThrownBy(()->attendService.disAttend(2L,2L,"false username"))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->attendService.disAttend(10L,2L,"true username2"))
                .isInstanceOf(NotFoundMeetingExeption.class);
        assertThatThrownBy(()->attendService.disAttend(1L,2L,"true username2"))
                .isInstanceOf(NotFoundAttendException.class);
        assertThatThrownBy(()->attendService.disAttend(2L,1L,"true username1"))
                .isInstanceOf(NotWithdrawException.class);
        DisAttendResponse disAttendResponse1 = attendService.disAttend(1L, 1L, "true username1");
        DisAttendResponse disAttendResponse2 = attendService.disAttend(3L, 1L, "true username2");
        assertThat(disAttendResponse1)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);
        assertThat(disAttendResponse2)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);
    }
}
