package spring.myproject.domain.alarm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import spring.myproject.common.exception.DbException;
import spring.myproject.common.exception.ValidationException;
import spring.myproject.domain.alarm.service.AlarmService;
import spring.myproject.dto.response.alarm.AlarmResponse;

@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @PatchMapping("/alarm/{id}")
    public ResponseEntity<Object> checkAlarm(Long id){
        try {
            Boolean checked = alarmService.checkAlarm(id);



        }catch (DbException e){
            e.printStackTrace();
        }catch (ValidationException e){

        }
        return null;

    }

    @DeleteMapping("/alarm/{id}")
    public ResponseEntity<Object> deleteAlarm(Long id){
        try {
            Boolean deleted = alarmService.checkAlarm(id);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    @PostMapping("/alarm")
    public ResponseEntity<Object> addAlarm(){
        try {
            alarmService.addAlarm();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    @GetMapping("/alarm")
    public ResponseEntity<Object> alarmList(@RequestParam Integer page){
        try {
            alarmService.alarmList(page);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
}
