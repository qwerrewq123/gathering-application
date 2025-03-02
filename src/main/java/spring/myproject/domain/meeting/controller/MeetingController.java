package spring.myproject.domain.meeting.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import spring.myproject.domain.meeting.dto.response.*;
import spring.myproject.domain.meeting.service.MeetingService;
import spring.myproject.domain.meeting.dto.request.AddMeetingRequest;
import spring.myproject.domain.meeting.dto.request.UpdateMeetingRequest;


@RestController
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping("/meeting/{gatheringId}")
    public ResponseEntity<AddMeetingResponse> addMeeting(@RequestBody AddMeetingRequest addMeetingRequest,
                                                         @PathVariable Long gatheringId,
                                                         @AuthenticationPrincipal String username){

        AddMeetingResponse addMeetingResponse = meetingService.addMeeting(addMeetingRequest, username, gatheringId);
        return new ResponseEntity<>(addMeetingResponse, HttpStatus.OK);
    }

    @DeleteMapping("/meeting/{meetingId}")
    public ResponseEntity<Object> deleteMeeting(@AuthenticationPrincipal String username,
                                                @PathVariable Long meetingId){


        DeleteMeetingResponse deleteMeetingResponse = meetingService.deleteMeeting(username, meetingId);
        return new ResponseEntity<>(deleteMeetingResponse, HttpStatus.OK);
    }

    @PatchMapping("/updateMeeting/{meetingId}")
    public ResponseEntity<Object> updateMeeting(@RequestBody UpdateMeetingRequest updateMeetingRequest,
                                                @AuthenticationPrincipal String username,
                                                @PathVariable Long meetingId){


        UpdateMeetingResponse updateMeetingResponse = meetingService.updateMeeting(updateMeetingRequest, username, meetingId);
        return new ResponseEntity<>(updateMeetingResponse, HttpStatus.OK);
    }

    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity<MeetingResponse> meetingDetail(@PathVariable Long meetingId,
                                                         @AuthenticationPrincipal String username){
            MeetingResponse meetingResponse = meetingService.meetingDetail(meetingId,username);
            return new ResponseEntity<>(meetingResponse, HttpStatus.OK);
    }

    @GetMapping("/meetings")
    public ResponseEntity<Object> meetings(@RequestParam int pageNum,
                                            @RequestParam int pageSize,
                                            @RequestParam String title,
                                            @AuthenticationPrincipal String username){

        MeetingsResponse meetingsResponse = meetingService.meetings(pageNum, pageSize,username, title);
        return new ResponseEntity<>(meetingsResponse, HttpStatus.OK);
    }
}
