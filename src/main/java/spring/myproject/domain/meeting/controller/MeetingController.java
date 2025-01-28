package spring.myproject.domain.meeting.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import spring.myproject.domain.meeting.service.MeetingService;
import spring.myproject.dto.request.meeting.AddMeetingRequest;
import spring.myproject.dto.request.meeting.UpdateMeetingRequest;
import spring.myproject.dto.response.meeting.AddMeetingResponse;
import spring.myproject.dto.response.meeting.MeetingListResponse;
import spring.myproject.dto.response.meeting.MeetingResponse;


@RestController
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping("/meeting/{gatheringId}")
    public ResponseEntity<Object> addMeeting(@RequestBody AddMeetingRequest addMeetingRequest,
                                             @PathVariable Long gatheringId,
                                             @AuthenticationPrincipal String username){


        try {
            meetingService.addMeeting(addMeetingRequest,username,gatheringId);
            return new ResponseEntity<>(AddMeetingResponse.builder()
                    .code("SU")
                    .message("Success")
                    .build(), HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }


    }

    @DeleteMapping("/meeting/{meetingId}")
    public ResponseEntity<Object> deleteMeeting(@AuthenticationPrincipal String username,
                                                @PathVariable Long meetingId){


        try {
            meetingService.deleteMeeting(username,meetingId);
            return new ResponseEntity<>(AddMeetingResponse.builder()
                    .code("SU")
                    .message("Success")
                    .build(), HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }


    }


    @PatchMapping("/updateMeeting/{meetingId}")
    public ResponseEntity<Object> updateMeeting(@RequestBody UpdateMeetingRequest updateMeetingRequest,
                                                @AuthenticationPrincipal String username,
                                                @PathVariable Long meetingId){


        try {
            meetingService.updateMeeting(updateMeetingRequest,username,meetingId);
            return new ResponseEntity<>(AddMeetingResponse.builder()
                    .code("SU")
                    .message("Success")
                    .build(), HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }


    }

    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity<Object> meetingDetail(@PathVariable Long meetingId,
                                                @AuthenticationPrincipal String username){
        try {
            MeetingResponse meetingResponse = meetingService.meetingDetail(meetingId,username);
            return new ResponseEntity<>(meetingResponse,HttpStatus.OK);



        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }


    @GetMapping("/meetings")
    public ResponseEntity<Object> meetings(@RequestParam int pageNum,
                                            @RequestParam String title,
                                            @AuthenticationPrincipal String username){
        try {
            Page<MeetingListResponse> meetingListResponses = meetingService.meetings(pageNum,username,title);
            return new ResponseEntity<>(meetingListResponses,HttpStatus.OK);



        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

}
