package spring.myproject.domain.attend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.myproject.domain.attend.service.AttendService;
import spring.myproject.dto.response.attend.AddAttendResponse;

@RestController
@RequiredArgsConstructor
public class AttendController {

    private final AttendService attendService;

    @PostMapping("/meeting/{meetingId}/attend")
    public ResponseEntity<Object> addAttend(@PathVariable Long meetingId,
                                            @AuthenticationPrincipal String username
                                            ){
        try {

            attendService.addAttend(meetingId,username);
            return new ResponseEntity<>(AddAttendResponse.builder()
                    .code("SU")
                    .message("Success"), HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }


    @PostMapping("/meeting/{meetingId}/disAttend/{attendId}")
    public ResponseEntity<Object> disAttend(@PathVariable Long meetingId,
                                            @PathVariable Long attendId,
                                            @AuthenticationPrincipal String username){
        try {

            attendService.disAttend(meetingId,attendId,username);
            return new ResponseEntity<>(AddAttendResponse.builder()
                    .code("SU")
                    .message("Success"), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/meeting/{meetingId}/permitAttend/{attendId}")
    public ResponseEntity<Object> permitAttend(@PathVariable Long meetingId,
                                               @PathVariable Long attendId,
                                               @AuthenticationPrincipal String username){
        try {
            attendService.permitAttend(meetingId,attendId,username);
            return new ResponseEntity<>(AddAttendResponse.builder()
                    .code("SU")
                    .message("Success"), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
