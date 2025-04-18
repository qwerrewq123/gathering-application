package spring.myproject.controller.meeting;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.common.annotation.Id;
import spring.myproject.service.meeting.MeetingService;

import java.io.IOException;

import static spring.myproject.dto.request.meeting.MeetingRequestDto.*;
import static spring.myproject.dto.response.meeting.MeetingResponseDto.*;


@RestController
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping("/gathering/{gatheringId}/meeting")
    public ResponseEntity<AddMeetingResponse> addMeeting(@RequestPart AddMeetingRequest addMeetingRequest,
                                                         @RequestPart MultipartFile file,
                                                         @PathVariable Long gatheringId,
                                                         @Id Long userId) throws IOException {

        AddMeetingResponse addMeetingResponse = meetingService.addMeeting(addMeetingRequest, userId, gatheringId,file);
        return new ResponseEntity<>(addMeetingResponse, HttpStatus.OK);
    }

    @DeleteMapping("/gathering/{gatheringId}/meeting/{meetingId}")
    public ResponseEntity<Object> deleteMeeting(@Id Long userId,
                                                @PathVariable Long meetingId,
                                                @PathVariable Long gatheringId) {


        DeleteMeetingResponse deleteMeetingResponse = meetingService.deleteMeeting(userId, meetingId,gatheringId);
        return new ResponseEntity<>(deleteMeetingResponse, HttpStatus.OK);
    }

    @PatchMapping("/gathering/{gatheringId}/meeting/{meetingId}")
    public ResponseEntity<Object> updateMeeting(@RequestPart UpdateMeetingRequest updateMeetingRequest,
                                                @RequestPart MultipartFile file,
                                                @Id Long userId,
                                                @PathVariable Long meetingId,
                                                @PathVariable Long gatheringId) throws IOException {


        UpdateMeetingResponse updateMeetingResponse = meetingService.updateMeeting(updateMeetingRequest, userId, meetingId,file,gatheringId);
        return new ResponseEntity<>(updateMeetingResponse, HttpStatus.OK);
    }

    @GetMapping("/gathering/{gatheringId}/meeting/{meetingId}")
    public ResponseEntity<MeetingResponse> meetingDetail(@PathVariable Long meetingId,
                                                         @Id Long userId,
                                                         @PathVariable Long gatheringId){
            MeetingResponse meetingResponse = meetingService.meetingDetail(meetingId,userId,gatheringId);
            return new ResponseEntity<>(meetingResponse, HttpStatus.OK);
    }

    @GetMapping("/meetings")
    public ResponseEntity<Object> meetings(@RequestParam int pageNum,
                                           @RequestParam int pageSize,
                                           @RequestParam(defaultValue = "") String title,
                                           @Id Long userId){

        MeetingsResponse meetingsResponse = meetingService.meetings(pageNum, pageSize,userId, title);
        return new ResponseEntity<>(meetingsResponse, HttpStatus.OK);
    }
}
