package spring.myproject.controller.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.myproject.common.annotation.Id;
import spring.myproject.service.alarm.AlarmService;

import static spring.myproject.dto.response.alarm.AlarmResponseDto.*;

@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @PatchMapping("/alarm/{id}")
    public ResponseEntity<CheckAlarmResponse> checkAlarm(Long id, @Id Long userId){

        CheckAlarmResponse checkAlarmResponse = alarmService.checkAlarm(id, userId);
        return new ResponseEntity<>(checkAlarmResponse, HttpStatus.OK);
    }

    @DeleteMapping("/alarm/{id}")
    public ResponseEntity<DeleteAlarmResponse> deleteAlarm(Long id, @Id Long userId){

        DeleteAlarmResponse deleteAlarmResponse = alarmService.deleteAlarm(id, userId);
        return new ResponseEntity<>(deleteAlarmResponse, HttpStatus.OK);
    }

    @GetMapping("/alarm")
    public ResponseEntity<AlarmResponses> alarmList(@RequestParam int page,
                                                    @Id Long userId,
                                            @RequestParam Boolean checked){

        AlarmResponses alarmResponses = alarmService.alarmList(page, userId, checked);
        return new ResponseEntity<>(alarmResponses, HttpStatus.OK);
    }
}
