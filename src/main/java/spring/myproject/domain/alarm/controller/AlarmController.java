package spring.myproject.domain.alarm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import spring.myproject.domain.alarm.service.AlarmService;
import spring.myproject.dto.request.alarm.AddAlarmRequest;
import spring.myproject.dto.response.alarm.AddAlarmResponse;
import spring.myproject.dto.response.alarm.AlarmResponse;
import spring.myproject.dto.response.alarm.CheckAlarmResponse;
import spring.myproject.dto.response.alarm.DeleteAlarmResponse;

@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @PatchMapping("/alarm/{id}")
    public ResponseEntity<Object> checkAlarm(Long id,@AuthenticationPrincipal String username){
        try {
            alarmService.checkAlarm(id,username);

            return new ResponseEntity<>(CheckAlarmResponse.builder()
                    .code("SU")
                    .message("Success"), HttpStatus.OK);



        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }

    @DeleteMapping("/alarm/{id}")
    public ResponseEntity<Object> deleteAlarm(Long id,@AuthenticationPrincipal String username){
        try {
            alarmService.deleteAlarm(id,username);
            return new ResponseEntity<>(DeleteAlarmResponse.builder()
                    .code("SU")
                    .message("Success"), HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }


    }

    @GetMapping("/alarm")
    public ResponseEntity<Object> alarmList(@RequestParam int page,
                                            @AuthenticationPrincipal String username,
                                            @RequestParam Boolean checked){
        try {
            Page<AlarmResponse> alarmResponses = alarmService.alarmList(page, username,checked);
            return new ResponseEntity<>(alarmResponses,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }


    }

    @PostMapping("/alarm")
    public ResponseEntity<Object> addAlarm(@RequestBody AddAlarmRequest addAlarmRequest, @AuthenticationPrincipal String username){
        try {
            alarmService.addAlarm(addAlarmRequest,username);
            return new ResponseEntity<>(AddAlarmResponse.builder()
                    .code("SU")
                    .message("Success"),HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }


    }
}
