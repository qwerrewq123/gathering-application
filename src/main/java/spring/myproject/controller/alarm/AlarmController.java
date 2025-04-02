package spring.myproject.controller.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.myproject.common.Username;
import spring.myproject.dto.response.alarm.AlarmResponseDto;
import spring.myproject.service.alarm.AlarmService;

import static spring.myproject.dto.response.alarm.AlarmResponseDto.*;

@RestController
@RequiredArgsConstructor
public class AlarmController {
    //TODO : 유저가 등록하면 알람, 유저가 모임 신청 알람
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
    public ResponseEntity<AlarmResponses> alarmList(@RequestParam int page,
                                            @Username String username,
                                            @RequestParam Boolean checked){

        AlarmResponses alarmResponses = alarmService.alarmList(page, username, checked);
        return new ResponseEntity<>(alarmResponses, HttpStatus.OK);
    }
}
