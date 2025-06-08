package spring.myproject.utils.change;

import spring.myproject.dto.request.meeting.MeetingRequestDto;
import spring.myproject.entity.image.Image;
import spring.myproject.entity.meeting.Meeting;

import static spring.myproject.dto.request.meeting.MeetingRequestDto.*;


public class ChangeFactory {
    public static void changeMeeting(Meeting meeting, UpdateMeetingRequest updateMeetingRequest, Image image) {
        meeting.setTitle(updateMeetingRequest.getTitle());
        meeting.setContent(updateMeetingRequest.getContent());
        meeting.setEndDate(updateMeetingRequest.getEndDate());
        meeting.setMeetingDate(updateMeetingRequest.getMeetingDate());
        meeting.setImage(image);
    }
}
