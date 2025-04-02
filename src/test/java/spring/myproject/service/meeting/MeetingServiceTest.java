package spring.myproject.service.meeting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.entity.attend.Attend;
import spring.myproject.entity.fcm.Topic;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.meeting.Meeting;
import spring.myproject.entity.user.Role;
import spring.myproject.entity.user.User;
import spring.myproject.exception.gathering.NotFoundGatheringException;
import spring.myproject.exception.meeting.NotAuthorizeException;
import spring.myproject.exception.meeting.NotFoundMeetingExeption;
import spring.myproject.exception.user.NotFoundUserException;
import spring.myproject.repository.attend.AttendRepository;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.repository.meeting.MeetingRepository;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.common.s3.S3ImageUploadService;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static spring.myproject.dto.request.meeting.MeetingRequestDto.*;
import static spring.myproject.dto.response.meeting.MeetingResponseDto.*;
import static spring.myproject.utils.ConstClass.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MeetingServiceTest {
    @Autowired
    MeetingService meetingService;
    @MockitoBean
    UserRepository userRepository;
    @MockitoBean
    MeetingRepository meetingRepository;
    @MockitoBean
    GatheringRepository gatheringRepository;
    @MockitoBean
    AttendRepository attendRepository;
    @MockitoBean
    S3ImageUploadService s3ImageUploadService;
    @MockitoBean
    ImageRepository imageRepository;

    @Test
    void addMeeting() throws IOException {
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        User mockUser = new User(1L,"true username","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        Gathering mockGathering = new Gathering(1L,null,null,null,null,mockUser,0,null,null, Topic.builder().topicName("topicname").build());
        when(s3ImageUploadService.upload(any(MultipartFile.class))).thenReturn("url");
        when(userRepository.findByUsername("true username")).thenReturn(Optional.of(mockUser));
        when(userRepository.findByUsername("false username")).thenReturn(Optional.empty());
        when(gatheringRepository.findById(1L)).thenReturn(Optional.of(mockGathering));
        when(gatheringRepository.findById(2L)).thenReturn(Optional.empty());
        AddMeetingRequest addMeetingRequest = new AddMeetingRequest();
        assertThatThrownBy(()->meetingService.addMeeting(addMeetingRequest,"false username",2L,mockMultipartFile))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->meetingService.addMeeting(addMeetingRequest,"true username",2L,mockMultipartFile))
                .isInstanceOf(NotFoundGatheringException.class);
        AddMeetingResponse addMeetingResponse = meetingService.addMeeting(addMeetingRequest, "true username", 1L, mockMultipartFile);
        assertThat(addMeetingResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);
    }
    @Test
    void updateMeeting() throws IOException {
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        User mockUser1 = new User(1L,"true username1","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        User mockUser2 = new User(2L,"true username2","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        Meeting mockMeeting = Meeting.builder().createdBy(mockUser1).count(1).build();
        when(userRepository.findByUsername("true username1")).thenReturn(Optional.of(mockUser1));
        when(userRepository.findByUsername("true username2")).thenReturn(Optional.of(mockUser2));
        when(userRepository.findByUsername("false username")).thenReturn(Optional.empty());
        when(meetingRepository.findById(1L)).thenReturn(Optional.of(mockMeeting));
        when(meetingRepository.findById(2L)).thenReturn(Optional.empty());
        when(s3ImageUploadService.upload(any(MultipartFile.class))).thenReturn("url");
        UpdateMeetingRequest updateMeetingRequest = UpdateMeetingRequest.builder().build();
        assertThatThrownBy(()->meetingService.updateMeeting(updateMeetingRequest,"false username",2L,mockMultipartFile))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->meetingService.updateMeeting(updateMeetingRequest,"true username2",2L,mockMultipartFile))
                .isInstanceOf(NotFoundMeetingExeption.class);
        assertThatThrownBy(()->meetingService.updateMeeting(updateMeetingRequest,"true username2",1L,mockMultipartFile))
                .isInstanceOf(NotAuthorizeException.class);
        UpdateMeetingResponse updateMeetingResponse = meetingService.updateMeeting(updateMeetingRequest, "true username1", 1L, mockMultipartFile);
        assertThat(updateMeetingResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);
    }
    @Test
    void deleteMeeting(){
        User mockUser1 = new User(1L,"true username1","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        User mockUser2 = new User(2L,"true username2","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        Meeting mockMeeting = Meeting.builder().createdBy(mockUser1).count(1).build();
        when(userRepository.findByUsername("true username1")).thenReturn(Optional.of(mockUser1));
        when(userRepository.findByUsername("true username2")).thenReturn(Optional.of(mockUser2));
        when(userRepository.findByUsername("false username")).thenReturn(Optional.empty());
        when(meetingRepository.findById(1L)).thenReturn(Optional.of(mockMeeting));
        when(meetingRepository.findById(2L)).thenReturn(Optional.empty());
        when(attendRepository.findByUserIdAndMeetingIdAndTrue(anyLong(),anyLong())).thenReturn(mock(Attend.class));
        assertThatThrownBy(()->meetingService.deleteMeeting("false username",2L))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->meetingService.deleteMeeting("true username1",2L))
                .isInstanceOf(NotFoundMeetingExeption.class);
        assertThatThrownBy(()->meetingService.deleteMeeting("true username2",1L))
                .isInstanceOf(NotAuthorizeException.class);
        DeleteMeetingResponse deleteMeetingResponse = meetingService.deleteMeeting("true username1", 1L);
        assertThat(deleteMeetingResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);
    }
}
