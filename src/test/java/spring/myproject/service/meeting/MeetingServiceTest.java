package spring.myproject.service.meeting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.common.async.AsyncService;
import spring.myproject.entity.attend.Attend;
import spring.myproject.entity.fcm.Topic;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.meeting.Meeting;
import spring.myproject.entity.user.Role;
import spring.myproject.entity.user.User;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.common.exception.meeting.NotAuthorizeException;
import spring.myproject.common.exception.meeting.NotFoundMeetingExeption;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.repository.attend.AttendRepository;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.repository.meeting.MeetingRepository;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.common.s3.S3ImageUploadService;
import spring.myproject.service.recommend.RecommendService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
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
    RecommendService recommendService;
    @MockitoBean
    AsyncService asyncService;

    @Test
    void addMeeting() throws IOException {
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        User mockUser = new User(1L,"true username","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        Gathering mockGathering = new Gathering(1L,null,null,null,null,mockUser,0,null,null, Topic.builder().build());
        when(s3ImageUploadService.upload(any(MultipartFile.class))).thenReturn("url");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        when(gatheringRepository.findTopicById(1L)).thenReturn(Optional.of(mockGathering));
        when(gatheringRepository.findTopicById(2L)).thenReturn(Optional.empty());
        doNothing().when(recommendService).addScore(anyLong(),anyInt());
        doNothing().when(asyncService).sendTopic(any());
        AddMeetingRequest addMeetingRequest = new AddMeetingRequest();
        assertThatThrownBy(()->meetingService.addMeeting(addMeetingRequest,2L,2L,mockMultipartFile))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->meetingService.addMeeting(addMeetingRequest,1L,2L,mockMultipartFile))
                .isInstanceOf(NotFoundGatheringException.class);
        AddMeetingResponse addMeetingResponse = meetingService.addMeeting(addMeetingRequest, 1L, 1L, mockMultipartFile);
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
        Gathering mockGathering = new Gathering(1L,null,null,null,null,mockUser1,0,null,null, Topic.builder().build());
        Meeting mockMeeting = Meeting.builder().createdBy(mockUser1).count(1).meetingDate(LocalDateTime.now()).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser2));
        when(userRepository.findById(3L)).thenReturn(Optional.empty());
        when(gatheringRepository.findTopicById(1L)).thenReturn(Optional.of(mockGathering));
        when(gatheringRepository.findTopicById(2L)).thenReturn(Optional.empty());
        when(meetingRepository.findById(1L)).thenReturn(Optional.of(mockMeeting));
        when(meetingRepository.findById(2L)).thenReturn(Optional.empty());
        when(s3ImageUploadService.upload(any(MultipartFile.class))).thenReturn("url");
        doNothing().when(asyncService).sendTopic(any());
        UpdateMeetingRequest updateMeetingRequest = UpdateMeetingRequest.builder().meetingDate(LocalDateTime.now().plusHours(1)).build();
        assertThatThrownBy(()->meetingService.updateMeeting(updateMeetingRequest,3L,2L,mockMultipartFile,2L))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->meetingService.updateMeeting(updateMeetingRequest,2L,2L,mockMultipartFile,2L))
                .isInstanceOf(NotFoundGatheringException.class);
        assertThatThrownBy(()->meetingService.updateMeeting(updateMeetingRequest,2L,2L,mockMultipartFile,1L))
                .isInstanceOf(NotFoundMeetingExeption.class);
        assertThatThrownBy(()->meetingService.updateMeeting(updateMeetingRequest,2L,1L,mockMultipartFile,1L))
                .isInstanceOf(NotAuthorizeException.class);
        UpdateMeetingResponse updateMeetingResponse = meetingService.updateMeeting(updateMeetingRequest, 1L, 1L, mockMultipartFile,1L);
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
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser2));
        when(userRepository.findById(3L)).thenReturn(Optional.empty());
        when(meetingRepository.findById(1L)).thenReturn(Optional.of(mockMeeting));
        when(meetingRepository.findById(2L)).thenReturn(Optional.empty());
        doNothing().when(meetingRepository).delete(any(Meeting.class));
        doNothing().when(recommendService).addScore(anyLong(),anyInt());
        assertThatThrownBy(()->meetingService.deleteMeeting(3L,2L,1L))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->meetingService.deleteMeeting(2L,2L,1L))
                .isInstanceOf(NotFoundMeetingExeption.class);
        assertThatThrownBy(()->meetingService.deleteMeeting(2L,1L,1L))
                .isInstanceOf(NotAuthorizeException.class);
        DeleteMeetingResponse deleteMeetingResponse = meetingService.deleteMeeting(1L, 1L,1L);
        assertThat(deleteMeetingResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);
    }
}
