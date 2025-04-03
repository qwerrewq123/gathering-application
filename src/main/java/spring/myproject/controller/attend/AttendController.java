package spring.myproject.controller.attend;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.myproject.common.Username;
import spring.myproject.dto.response.attend.AttendResponseDto;
import spring.myproject.service.attend.AttendService;

import static spring.myproject.dto.response.attend.AttendResponseDto.*;

@RestController
@RequiredArgsConstructor
public class AttendController {

    private final AttendService attendService;

    @PostMapping("/gathering/{gatheringId}/meeting/{meetingId}/attend")
    public ResponseEntity<AddAttendResponse> addAttend(@PathVariable Long meetingId,
                                                       @Username String username,
                                                       @PathVariable Long gatheringId
                                            ){

        AddAttendResponse addAttendResponse = attendService.addAttend(meetingId, username,gatheringId);
        return new ResponseEntity<>(addAttendResponse, HttpStatus.OK);
    }


    @PostMapping("/gathering/{gatheringId}/meeting/{meetingId}/disAttend/{attendId}")
    public ResponseEntity<DisAttendResponse> disAttend(@PathVariable Long meetingId,
                                                       @PathVariable Long attendId,
                                                       @Username String username,
                                                       @PathVariable Long gatheringId){

        DisAttendResponse disAttendResponse = attendService.disAttend(meetingId, attendId, username,gatheringId);
        return new ResponseEntity<>(disAttendResponse, HttpStatus.OK);
    }

    @PostMapping("/meeting/{meetingId}/permitAttend/{attendId}")
    public ResponseEntity<PermitAttendResponse> permitAttend(@PathVariable Long meetingId,
                                               @PathVariable Long attendId,
                                               @Username String username){
        PermitAttendResponse permitAttendResponse = attendService.permitAttend(meetingId, attendId, username);
        return new ResponseEntity<>(permitAttendResponse, HttpStatus.OK);
    }
}
