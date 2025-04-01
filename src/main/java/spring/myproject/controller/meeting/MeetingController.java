package spring.myproject.controller.meeting;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.common.Username;
import spring.myproject.dto.response.meeting.*;
import spring.myproject.service.meeting.MeetingService;

import java.io.IOException;

import static spring.myproject.dto.request.meeting.MeetingRequestDto.*;
import static spring.myproject.dto.response.meeting.MeetingResponseDto.*;


@RestController
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping("/meeting/{gatheringId}")
    public ResponseEntity<AddMeetingResponse> addMeeting(@RequestPart AddMeetingRequest addMeetingRequest,
                                                         @RequestPart MultipartFile file,
                                                         @PathVariable Long gatheringId,
                                                         @Username String username) throws IOException {

        AddMeetingResponse addMeetingResponse = meetingService.addMeeting(addMeetingRequest, username, gatheringId,file);
        return new ResponseEntity<>(addMeetingResponse, HttpStatus.OK);
    }

    @DeleteMapping("/meeting/{meetingId}")
    public ResponseEntity<Object> deleteMeeting(@Username String username,
                                                @PathVariable Long meetingId){


        DeleteMeetingResponse deleteMeetingResponse = meetingService.deleteMeeting(username, meetingId);
        return new ResponseEntity<>(deleteMeetingResponse, HttpStatus.OK);
    }

    @PatchMapping("/updateMeeting/{meetingId}")
    public ResponseEntity<Object> updateMeeting(@RequestPart UpdateMeetingRequest updateMeetingRequest,
                                                @RequestPart MultipartFile file,
                                                @Username String username,
                                                @PathVariable Long meetingId) throws IOException {


        UpdateMeetingResponse updateMeetingResponse = meetingService.updateMeeting(updateMeetingRequest, username, meetingId,file);
        return new ResponseEntity<>(updateMeetingResponse, HttpStatus.OK);
    }

    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity<MeetingResponse> meetingDetail(@PathVariable Long meetingId,
                                                         @Username String username){
            MeetingResponse meetingResponse = meetingService.meetingDetail(meetingId,username);
            return new ResponseEntity<>(meetingResponse, HttpStatus.OK);
    }

    @GetMapping("/meetings")
    public ResponseEntity<Object> meetings(@RequestParam int pageNum,
                                            @RequestParam int pageSize,
                                            @RequestParam(defaultValue = "") String title,
                                            @Username String username){

        MeetingsResponse meetingsResponse = meetingService.meetings(pageNum, pageSize,username, title);
        return new ResponseEntity<>(meetingsResponse, HttpStatus.OK);
    }
}
