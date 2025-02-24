package spring.myproject.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.domain.alarm.Alarm;
import spring.myproject.domain.alarm.dto.response.*;
import spring.myproject.domain.alarm.repository.AlarmRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;
import spring.myproject.domain.alarm.dto.request.AddAlarmRequest;
import spring.myproject.domain.alarm.exception.NotFoundAlarmException;
import spring.myproject.domain.user.exception.NotFoundUserException;

import java.time.LocalDateTime;

import static spring.myproject.util.ConstClass.*;

@RequiredArgsConstructor
@Service
@Transactional
public class AlarmService {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;


    public CheckAlarmResponse checkAlarm(Long id, String username) {

            userRepository.findByUsername(username).orElseThrow(()-> new NotFoundUserException("no exist User!!"));
            Alarm alarm = alarmRepository.findById(id).orElseThrow(() ->  new NotFoundAlarmException("no exist alarm!!"));
            alarm.setChecked(true);
            return CheckAlarmResponse.builder()
                    .code(SUCCESS_CODE)
                    .message(SUCCESS_MESSAGE)
                    .build();
    }

    public DeleteAlarmResponse deleteAlarm(Long id, String username) {

            userRepository.findByUsername(username).orElseThrow(()-> new NotFoundUserException("no exist User!!"));
            Alarm alarm = alarmRepository.findById(id).orElseThrow(() ->  new IllegalArgumentException("no exist alarm!!"));
            alarmRepository.delete(alarm);
            return DeleteAlarmResponse.builder()
                    .code(SUCCESS_CODE)
                    .message(SUCCESS_MESSAGE)
                    .build();
    }

    public AlarmResponsePage alarmList(Integer page, String username, Boolean checked) {

            User user = userRepository.findByUsername(username).orElseThrow(()-> new NotFoundUserException("no exist User!!"));
            Page<AlarmResponse> alarmResponsePage = getAlarmResponses(page, user,checked);
            return AlarmResponsePage.builder()
                    .code(SUCCESS_CODE)
                    .message(SUCCESS_MESSAGE)
                    .page(alarmResponsePage)
                    .build();

    }

    public AddAlarmResponse addAlarm(AddAlarmRequest addAlarmRequest, String username) {

            User user = userRepository.findByUsername(username).orElseThrow(()-> new NotFoundUserException("no exist User!!"));
            Alarm alarm = Alarm.builder()
                    .date(LocalDateTime.now())
                    .content(addAlarmRequest.getContent())
                    .checked(false)
                    .user(user)
                    .build();
            alarmRepository.save(alarm);
            return AddAlarmResponse.builder()
                    .code(SUCCESS_CODE)
                    .message(SUCCESS_MESSAGE)
                    .build();
    }

    private Page<AlarmResponse> getAlarmResponses(Integer page, User user,boolean checked) {
        if(checked == true){
            PageRequest pageRequest = PageRequest.of(page - 1, 10);
            Page<Alarm> alarmPage = alarmRepository.findCheckedAlarmPage(pageRequest, user.getId());
            return alarmPage.map(a -> AlarmResponse.builder()
                    .date(a.getDate())
                    .content(a.getContent())
                    .checked(a.getChecked())
                    .build());
        }else{
            PageRequest pageRequest = PageRequest.of(page - 1, 10);
            Page<Alarm> alarmPage = alarmRepository.findUncheckedAlarmPage(pageRequest, user.getId());
            return alarmPage.map(a -> AlarmResponse.builder()
                    .date(a.getDate())
                    .content(a.getContent())
                    .checked(a.getChecked())
                    .build());
        }
    }
}
