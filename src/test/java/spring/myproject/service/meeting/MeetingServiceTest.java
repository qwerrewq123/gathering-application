package spring.myproject.service.meeting;

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
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.common.async.AsyncService;
import spring.myproject.dto.request.fcm.TopicNotificationRequestDto;
import spring.myproject.dto.response.alarm.AlarmResponseDto;
import spring.myproject.entity.alarm.Alarm;
import spring.myproject.entity.attend.Attend;
import spring.myproject.entity.fcm.Topic;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.image.Image;
import spring.myproject.entity.meeting.Meeting;
import spring.myproject.entity.user.Role;
import spring.myproject.entity.user.User;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.common.exception.meeting.NotAuthorizeException;
import spring.myproject.common.exception.meeting.NotFoundMeetingExeption;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.repository.alarm.AlarmRepository;
import spring.myproject.repository.attend.AttendRepository;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.repository.meeting.MeetingRepository;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.common.s3.S3ImageUploadService;
import spring.myproject.service.alarm.AlarmService;
import spring.myproject.service.recommend.RecommendService;
import spring.myproject.utils.MockData;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static spring.myproject.dto.request.meeting.MeetingRequestDto.*;
import static spring.myproject.dto.response.meeting.MeetingResponseDto.*;
import static spring.myproject.utils.ConstClass.*;
import static spring.myproject.utils.MockData.*;

@ExtendWith(MockitoExtension.class)
public class MeetingServiceTest {
    @InjectMocks
    MeetingService meetingService;
    @Mock
    UserRepository userRepository;
    @Mock
    MeetingRepository meetingRepository;
    @Mock
    GatheringRepository gatheringRepository;
    @Mock
    AttendRepository attendRepository;
    @Mock
    S3ImageUploadService s3ImageUploadService;
    @Mock
    RecommendService recommendService;
    @Mock
    AsyncService asyncService;
    @Mock
    ImageRepository imageRepository;
    @Mock
    AlarmService alarmService;
    @DisplayName("throws NotFoundUserException")
    @Test
    void addMeetingThrowsNotFoundUserException(){
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());
        AddMeetingRequest addMeetingRequest = AddMeetingRequest.builder()
                .build();
        assertThatThrownBy(()->meetingService.addMeeting(addMeetingRequest,1L,1L,mockMultipartFile))
                .isInstanceOf(NotFoundUserException.class);
    }
    @DisplayName("throws NotFoundGatheringException")
    @Test
    void addMeetingThrowsNotFoundGatheringException(){
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        User mockUser = returnMockUser(1L,"true username","true password");
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(gatheringRepository.findTopicById(1L))
                .thenReturn(Optional.empty());
        AddMeetingRequest addMeetingRequest = AddMeetingRequest.builder()
                .build();
        assertThatThrownBy(()->meetingService.addMeeting(addMeetingRequest,1L,1L,mockMultipartFile))
                .isInstanceOf(NotFoundGatheringException.class);
    }

    @DisplayName("Return Normal Response")
    @Test
    void addMeeting() throws IOException {
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        User mockUser1 = returnMockUser(1L,"true username","true password");
        User mockUser2 = returnMockUser(2L,"true username","true password");
        User mockUser3 = returnMockUser(3L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1));
        when(gatheringRepository.findTopicById(1L)).thenReturn(Optional.of(mockGathering));
        when(s3ImageUploadService.upload(any(MultipartFile.class)))
                .thenReturn("url");
        when(mockMultipartFile.getContentType())
                .thenReturn("image/png");
        when(imageRepository.save(any(Image.class)))
                .thenReturn(mock(Image.class));
        when(meetingRepository.save(any(Meeting.class)))
                .thenReturn(mock(Meeting.class));
        when(attendRepository.save(any(Attend.class)))
                .thenReturn(mock(Attend.class));
        doNothing().when(recommendService).addScore(anyLong(),anyInt());
        when(userRepository.findEnrollmentById(1L,1L))
                .thenReturn(List.of(mockUser2,mockUser3));
        doNothing().when(alarmService).saveAll(any(List.class));
        doNothing().when(asyncService).sendTopic(any(TopicNotificationRequestDto.class));
        AddMeetingRequest addMeetingRequest = AddMeetingRequest.builder()
                .build();

        AddMeetingResponse addMeetingResponse = meetingService.addMeeting(addMeetingRequest, 1L, 1L, mockMultipartFile);

        assertThat(addMeetingResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);
    }
    @DisplayName("Throws NotFoundUserException")
    @Test
    void deleteMeetingThrowsNotFoundUserException(){
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->meetingService.deleteMeeting(1L,1L,1L))
                .isInstanceOf(NotFoundUserException.class);
    }
    @DisplayName("Throws NotFoundMeetingException")
    @Test
    void deleteMeetingThrowsNotFoundMeetingException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(meetingRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->meetingService.deleteMeeting(1L,1L,1L))
                .isInstanceOf(NotFoundMeetingExeption.class);
    }
    @DisplayName("Throws NotAuthorizeException")
    @Test
    void deleteMeetingThrowsNotAuthorizeException(){
        User mockUser1 = returnMockUser(1L,"true username","true password");
        User mockUser2 = returnMockUser(2L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser1);
        Meeting mockMeeting = returnMockMeeting(mockGathering,mockUser1);
        when(userRepository.findById(2L))
                .thenReturn(Optional.of(mockUser2));
        when(meetingRepository.findById(1L))
                .thenReturn(Optional.of(mockMeeting));

        assertThatThrownBy(()->meetingService.deleteMeeting(2L,1L,1L))
                .isInstanceOf(NotAuthorizeException.class);
    }
    @DisplayName("Return Normal Response")
    @Test
    void deleteMeeting(){
        User mockUser = returnMockUser(1L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser);
        Meeting mockMeeting = returnMockMeeting(mockGathering,mockUser);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(meetingRepository.findById(1L))
                .thenReturn(Optional.of(mockMeeting));
        doNothing().when(meetingRepository).delete(any(Meeting.class));
        doNothing().when(recommendService).addScore(anyLong(),anyInt());

        DeleteMeetingResponse deleteMeetingResponse = meetingService.deleteMeeting(1L, 1L,1L);

        assertThat(deleteMeetingResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);
    }
    @DisplayName("Throws NotFoundUserException")
    @Test
    void updateMeetingThrowsNotFoundUserException(){
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());
        UpdateMeetingRequest updateMeetingRequest = UpdateMeetingRequest.builder()
                .build();
        assertThatThrownBy(()->meetingService.updateMeeting(updateMeetingRequest,1L,1L,mockMultipartFile,1L))
                .isInstanceOf(NotFoundUserException.class);
    }
    @DisplayName("Throws NotFoundGatheringException")
    @Test
    void updateMeetingThrowsNotFoundGatheringException(){
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        User mockUser = returnMockUser(1L,"true username","true password");
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(gatheringRepository.findTopicById(1L))
                .thenReturn(Optional.empty());
        UpdateMeetingRequest updateMeetingRequest = UpdateMeetingRequest.builder()
                .build();
        assertThatThrownBy(()->meetingService.updateMeeting(updateMeetingRequest,1L,1L,mockMultipartFile,1L))
                .isInstanceOf(NotFoundGatheringException.class);
    }
    @DisplayName("Throws NotFoundMeetingException")
    @Test
    void updateMeetingThrowsNotFoundMeetingException(){
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        User mockUser = returnMockUser(1L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(gatheringRepository.findTopicById(1L))
                .thenReturn(Optional.of(mockGathering));
        when(meetingRepository.findById(1L))
                .thenReturn(Optional.empty());
        UpdateMeetingRequest updateMeetingRequest = UpdateMeetingRequest.builder()
                .build();
        assertThatThrownBy(()->meetingService.updateMeeting(updateMeetingRequest,1L,1L,mockMultipartFile,1L))
                .isInstanceOf(NotFoundMeetingExeption.class);
    }
    @DisplayName("Throws NotAuthorizeException")
    @Test
    void updateMeetingThrowsNotAuthorizeException(){
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        User mockUser1 = returnMockUser(1L,"true username","true password");
        User mockUser2 = returnMockUser(2L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser1);
        Meeting mockMeeting = returnMockMeeting(mockGathering,mockUser1);
        when(userRepository.findById(2L))
                .thenReturn(Optional.of(mockUser2));
        when(gatheringRepository.findTopicById(1L))
                .thenReturn(Optional.of(mockGathering));
        when(meetingRepository.findById(1L))
                .thenReturn(Optional.of(mockMeeting));
        UpdateMeetingRequest updateMeetingRequest = UpdateMeetingRequest.builder()
                .build();
        assertThatThrownBy(()->meetingService.updateMeeting(updateMeetingRequest,2L,1L,mockMultipartFile,1L))
                .isInstanceOf(NotAuthorizeException.class);
    }

    @Test
    void updateMeeting() throws IOException {
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        User mockUser1 = returnMockUser(1L,"true username","true password");
        User mockUser2 = returnMockUser(1L,"true username","true password");
        User mockUser3 = returnMockUser(1L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser1);
        Meeting mockMeeting = returnMockMeeting(mockGathering,mockUser1);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser1));
        when(gatheringRepository.findTopicById(1L))
                .thenReturn(Optional.of(mockGathering));
        when(meetingRepository.findById(1L))
                .thenReturn(Optional.of(mockMeeting));
        when(gatheringRepository.findTopicById(1L))
                .thenReturn(Optional.of(mockGathering));
        when(meetingRepository.findById(1L))
                .thenReturn(Optional.of(mockMeeting));
        when(s3ImageUploadService.upload(any(MultipartFile.class)))
                .thenReturn("url");
        when(mockMultipartFile.getContentType())
                .thenReturn("image/png");
        when(imageRepository.save(any(Image.class)))
                .thenReturn(mock(Image.class));
        when(userRepository.findEnrollmentById(1L,1L))
                .thenReturn(List.of(mockUser2,mockUser3));
        UpdateMeetingRequest updateMeetingRequest = UpdateMeetingRequest.builder()
                .meetingDate(LocalDateTime.now().plusHours(1))
                .build();
        doNothing().when(alarmService).saveAll(any(List.class));
        doNothing().when(asyncService).sendTopic(any(TopicNotificationRequestDto.class));

        UpdateMeetingResponse updateMeetingResponse = meetingService.updateMeeting(updateMeetingRequest, 1L, 1L, mockMultipartFile,1L);

        assertThat(updateMeetingResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE, SUCCESS_MESSAGE);
    }
}
