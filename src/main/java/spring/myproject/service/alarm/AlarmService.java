package spring.myproject.service.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.dto.response.alarm.AlarmResponseDto;
import spring.myproject.entity.fcm.Alarm;
import spring.myproject.repository.alarm.AlarmRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.exception.alarm.NotFoundAlarmException;
import spring.myproject.exception.user.NotFoundUserException;

import java.util.List;

import static spring.myproject.dto.response.alarm.AlarmResponseDto.*;
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
    public AlarmResponses alarmList(Integer page, String username, Boolean checked) {

            User user = userRepository.findByUsername(username).orElseThrow(()-> new NotFoundUserException("no exist User!!"));
            return toAlarmResponses(page-1,user,checked);
    }

    private AlarmResponses toAlarmResponses(Integer pageNum, User user,boolean checked) {
        Page<Alarm> page = null;
        if(checked == true){
            PageRequest pageRequest = PageRequest.of(pageNum - 1, 10);
            page = alarmRepository.findCheckedAlarmPage(pageRequest, user.getId());
        }else{
            PageRequest pageRequest = PageRequest.of(pageNum - 1, 10);
            page = alarmRepository.findUncheckedAlarmPage(pageRequest, user.getId());
        }
        boolean hasNext = page.hasNext();
        List<AlarmElement> content = page.map(alarm -> AlarmElement.from(alarm)).getContent();
        return AlarmResponses.of(SUCCESS_CODE,SUCCESS_MESSAGE,content,hasNext);

    }
}
