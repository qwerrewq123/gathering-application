package spring.myproject.service.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.entity.fcm.Alarm;
import spring.myproject.dto.response.alarm.AlarmResponse;
import spring.myproject.dto.response.alarm.AlarmResponsePage;
import spring.myproject.dto.response.alarm.CheckAlarmResponse;
import spring.myproject.dto.response.alarm.DeleteAlarmResponse;
import spring.myproject.repository.alarm.AlarmRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.exception.alarm.NotFoundAlarmException;
import spring.myproject.exception.user.NotFoundUserException;

import static spring.myproject.utils.ConstClass.*;

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
            return CheckAlarmResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
    public DeleteAlarmResponse deleteAlarm(Long id, String username) {

            userRepository.findByUsername(username).orElseThrow(()-> new NotFoundUserException("no exist User!!"));
            Alarm alarm = alarmRepository.findById(id).orElseThrow(() ->  new IllegalArgumentException("no exist alarm!!"));
            alarmRepository.delete(alarm);
            return DeleteAlarmResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
    public AlarmResponsePage alarmList(Integer page, String username, Boolean checked) {

            User user = userRepository.findByUsername(username).orElseThrow(()-> new NotFoundUserException("no exist User!!"));
            Page<AlarmResponse> alarmResponsePage = getAlarmResponses(page, user,checked);
            return AlarmResponsePage.of(SUCCESS_CODE,SUCCESS_MESSAGE,alarmResponsePage);
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
