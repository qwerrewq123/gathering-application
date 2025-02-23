package spring.myproject.domain.alarm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import spring.myproject.domain.alarm.dto.response.AlarmResponsePage;
import spring.myproject.domain.alarm.dto.response.CheckAlarmResponse;
import spring.myproject.domain.alarm.dto.response.DeleteAlarmResponse;
import spring.myproject.domain.alarm.service.AlarmService;

@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @PatchMapping("/alarm/{id}")
    public ResponseEntity<CheckAlarmResponse> checkAlarm(Long id, @AuthenticationPrincipal String username){
        CheckAlarmResponse checkAlarmResponse = alarmService.checkAlarm(id, username);
        if(checkAlarmResponse.getCode().equals("SU")){
            return new ResponseEntity<>(checkAlarmResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(checkAlarmResponse, HttpStatus.BAD_REQUEST);
        }


    }

    @DeleteMapping("/alarm/{id}")
    public ResponseEntity<DeleteAlarmResponse> deleteAlarm(Long id, @AuthenticationPrincipal String username){
        DeleteAlarmResponse deleteAlarmResponse = alarmService.deleteAlarm(id, username);
        if(deleteAlarmResponse.getCode().equals("SU")){
            return new ResponseEntity<>(deleteAlarmResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(deleteAlarmResponse, HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping("/alarm")
    public ResponseEntity<Object> alarmList(@RequestParam int page,
                                            @AuthenticationPrincipal String username,
                                            @RequestParam Boolean checked){
        AlarmResponsePage alarmResponsePage = alarmService.alarmList(page, username, checked);
        if(alarmResponsePage.getCode().equals("SU")){
            return new ResponseEntity<>(alarmResponsePage, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(alarmResponsePage, HttpStatus.BAD_REQUEST);
        }


    }


}
