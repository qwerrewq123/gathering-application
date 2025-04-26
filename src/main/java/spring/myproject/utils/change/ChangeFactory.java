package spring.myproject.utils.change;

import spring.myproject.dto.request.meeting.MeetingRequestDto;
import spring.myproject.entity.image.Image;
import spring.myproject.entity.meeting.Meeting;


public class ChangeFactory {
    public static void changeMeeting(Meeting meeting, MeetingRequestDto.UpdateMeetingRequest updateMeetingRequest, Image image) {
        meeting.setTitle(updateMeetingRequest.getTitle());
        meeting.setContent(updateMeetingRequest.getContent());
        meeting.setEndDate(updateMeetingRequest.getEndDate());
        meeting.setMeetingDate(updateMeetingRequest.getMeetingDate());
        meeting.setImage(image);
    }
}
