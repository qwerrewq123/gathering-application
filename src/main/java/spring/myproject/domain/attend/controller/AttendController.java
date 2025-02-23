package spring.myproject.domain.attend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.myproject.annotation.Username;
import spring.myproject.domain.attend.service.AttendService;
import spring.myproject.dto.response.attend.AddAttendResponse;
import spring.myproject.dto.response.attend.DisAttendResponse;
import spring.myproject.dto.response.attend.PermitAttendResponse;

@RestController
@RequiredArgsConstructor
public class AttendController {

    private final AttendService attendService;

    @PostMapping("/meeting/{meetingId}/attend")
    public ResponseEntity<AddAttendResponse> addAttend(@PathVariable Long meetingId,
                                            @Username String username
                                            ){

        AddAttendResponse addAttendResponse = attendService.addAttend(meetingId, username);
        if(addAttendResponse.getCode().equals("SU")){
            return new ResponseEntity<>(addAttendResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(addAttendResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/meeting/{meetingId}/disAttend/{attendId}")
    public ResponseEntity<DisAttendResponse> disAttend(@PathVariable Long meetingId,
                                            @PathVariable Long attendId,
                                            @Username String username){

        DisAttendResponse disAttendResponse = attendService.disAttend(meetingId, attendId, username);
        if(disAttendResponse.getCode().equals("SU")){
            return new ResponseEntity<>(disAttendResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(disAttendResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/meeting/{meetingId}/permitAttend/{attendId}")
    public ResponseEntity<PermitAttendResponse> permitAttend(@PathVariable Long meetingId,
                                               @PathVariable Long attendId,
                                               @Username String username){
        PermitAttendResponse permitAttendResponse = attendService.permitAttend(meetingId, attendId, username);
        if(permitAttendResponse.getCode().equals("SU")){
            return new ResponseEntity<>(permitAttendResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(permitAttendResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
