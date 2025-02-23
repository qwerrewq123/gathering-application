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
        if(addMeetingResponse.getCode().equals("SU")){
            return new ResponseEntity<>(addMeetingResponse, HttpStatus.OK);

        }else {
            return new ResponseEntity<>(addMeetingResponse, HttpStatus.BAD_REQUEST);
        }


    }
    @DeleteMapping("/meeting/{meetingId}")
    public ResponseEntity<Object> deleteMeeting(@AuthenticationPrincipal String username,
                                                @PathVariable Long meetingId){


        DeleteMeetingResponse deleteMeetingResponse = meetingService.deleteMeeting(username, meetingId);
        if(deleteMeetingResponse.getCode().equals("SU")){
            return new ResponseEntity<>(deleteMeetingResponse, HttpStatus.OK);

        }else {
            return new ResponseEntity<>(deleteMeetingResponse, HttpStatus.BAD_REQUEST);
        }

    }


    @PatchMapping("/updateMeeting/{meetingId}")
    public ResponseEntity<Object> updateMeeting(@RequestBody UpdateMeetingRequest updateMeetingRequest,
                                                @AuthenticationPrincipal String username,
                                                @PathVariable Long meetingId){


        UpdateMeetingResponse updateMeetingResponse = meetingService.updateMeeting(updateMeetingRequest, username, meetingId);
        if(updateMeetingResponse.getCode().equals("SU")){
            return new ResponseEntity<>(updateMeetingResponse, HttpStatus.OK);

        }else {
            return new ResponseEntity<>(updateMeetingResponse, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity<MeetingResponse> meetingDetail(@PathVariable Long meetingId,
                                                         @AuthenticationPrincipal String username){
            MeetingResponse meetingResponse = meetingService.meetingDetail(meetingId,username);
        if(meetingResponse.getCode().equals("SU")){
            return new ResponseEntity<>(meetingResponse, HttpStatus.OK);

        }else {
            return new ResponseEntity<>(meetingResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/meetings")
    public ResponseEntity<Object> meetings(@RequestParam int pageNum,
                                            @RequestParam String title,
                                            @AuthenticationPrincipal String username){

        MeetingListResponse meetingListResponse = meetingService.meetings(pageNum, username, title);
        if(meetingListResponse.getCode().equals("SU")){
            return new ResponseEntity<>(meetingListResponse, HttpStatus.OK);

        }else {
            return new ResponseEntity<>(meetingListResponse, HttpStatus.BAD_REQUEST);
        }
    }

}
