package spring.myproject.domain.alarm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import spring.myproject.common.Username;
import spring.myproject.domain.alarm.dto.response.AlarmResponsePage;
import spring.myproject.domain.alarm.dto.response.CheckAlarmResponse;
import spring.myproject.domain.alarm.dto.response.DeleteAlarmResponse;
import spring.myproject.domain.alarm.service.AlarmService;

@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @PatchMapping("/alarm/{id}")
    public ResponseEntity<CheckAlarmResponse> checkAlarm(Long id, @Username String username){

        CheckAlarmResponse checkAlarmResponse = alarmService.checkAlarm(id, username);
        return new ResponseEntity<>(checkAlarmResponse, HttpStatus.OK);
    }

    @DeleteMapping("/alarm/{id}")
    public ResponseEntity<DeleteAlarmResponse> deleteAlarm(Long id, @Username String username){

        DeleteAlarmResponse deleteAlarmResponse = alarmService.deleteAlarm(id, username);
        return new ResponseEntity<>(deleteAlarmResponse, HttpStatus.OK);
    }

    @GetMapping("/alarm")
    public ResponseEntity<Object> alarmList(@RequestParam int page,
                                            @Username String username,
                                            @RequestParam Boolean checked){

        AlarmResponsePage alarmResponsePage = alarmService.alarmList(page, username, checked);
        return new ResponseEntity<>(alarmResponsePage, HttpStatus.OK);
    }
}
