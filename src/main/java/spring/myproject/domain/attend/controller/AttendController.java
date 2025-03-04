package spring.myproject.domain.attend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.myproject.common.Username;
import spring.myproject.domain.attend.service.AttendService;
import spring.myproject.domain.attend.dto.response.AddAttendResponse;
import spring.myproject.domain.attend.dto.response.DisAttendResponse;
import spring.myproject.domain.attend.dto.response.PermitAttendResponse;

@RestController
@RequiredArgsConstructor
public class AttendController {

    private final AttendService attendService;

    @PostMapping("/meeting/{meetingId}/attend")
    public ResponseEntity<AddAttendResponse> addAttend(@PathVariable Long meetingId,
                                            @Username String username
                                            ){

        AddAttendResponse addAttendResponse = attendService.addAttend(meetingId, username);
        return new ResponseEntity<>(addAttendResponse, HttpStatus.OK);
    }


    @PostMapping("/meeting/{meetingId}/disAttend/{attendId}")
    public ResponseEntity<DisAttendResponse> disAttend(@PathVariable Long meetingId,
                                            @PathVariable Long attendId,
                                            @Username String username){

        DisAttendResponse disAttendResponse = attendService.disAttend(meetingId, attendId, username);
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
