package spring.myproject.controller.attend;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.myproject.common.annotation.Id;
import spring.myproject.service.attend.AttendService;

import static spring.myproject.dto.response.attend.AttendResponseDto.*;

@RestController
@RequiredArgsConstructor
public class AttendController {

    private final AttendService attendService;

    @PostMapping("/gathering/{gatheringId}/meeting/{meetingId}/attend")
    public ResponseEntity<AddAttendResponse> addAttend(@PathVariable Long meetingId,
                                                       @Id Long userId,
                                                       @PathVariable Long gatheringId
                                            ){

        AddAttendResponse addAttendResponse = attendService.addAttend(meetingId, userId,gatheringId);
        return new ResponseEntity<>(addAttendResponse, HttpStatus.OK);
    }


    @PostMapping("/gathering/{gatheringId}/meeting/{meetingId}/disAttend")
    public ResponseEntity<DisAttendResponse> disAttend(@PathVariable Long meetingId,
                                                       @Id Long userId,
                                                       @PathVariable Long gatheringId){

        DisAttendResponse disAttendResponse = attendService.disAttend(meetingId, userId,gatheringId);
        return new ResponseEntity<>(disAttendResponse, HttpStatus.OK);
    }

    @PostMapping("/gathering/{gatheringId}/meeting/{meetingId}/permitAttend/{attendId}")
    public ResponseEntity<PermitAttendResponse> permitAttend(@PathVariable Long meetingId,
                                               @PathVariable Long attendId,
                                                             @Id Long userId){
        PermitAttendResponse permitAttendResponse = attendService.permitAttend(meetingId, attendId, userId);
        return new ResponseEntity<>(permitAttendResponse, HttpStatus.OK);
    }
}
