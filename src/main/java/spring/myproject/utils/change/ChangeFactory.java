package spring.myproject.utils.change;

import spring.myproject.dto.request.meeting.MeetingRequestDto;
import spring.myproject.entity.image.Image;
import spring.myproject.entity.meeting.Meeting;

import java.time.LocalDateTime;

public class ChangeFactory {
    public static void changeMeeting(Meeting meeting, MeetingRequestDto.UpdateMeetingRequest updateMeetingRequest, Image image) {
        meeting.setTitle(updateMeetingRequest.getTitle());
        meeting.setContent(updateMeetingRequest.getContent());
        meeting.setStartDate(updateMeetingRequest.getStartDate());
        meeting.setEndDate(updateMeetingRequest.getEndDate());
        meeting.setBoardDate(LocalDateTime.now());
        meeting.setImage(image);
    }
}
