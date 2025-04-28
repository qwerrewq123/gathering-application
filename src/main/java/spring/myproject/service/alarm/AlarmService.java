package spring.myproject.service.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.entity.alarm.Alarm;
import spring.myproject.repository.alarm.AlarmRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.common.exception.alarm.NotFoundAlarmException;
import spring.myproject.common.exception.user.NotFoundUserException;

import java.util.List;

import static spring.myproject.dto.response.alarm.AlarmResponseDto.*;
import static spring.myproject.utils.ConstClass.*;

@RequiredArgsConstructor
@Service
@Transactional
public class AlarmService {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    public CheckAlarmResponse checkAlarm(Long id, Long userId) {

            userRepository.findById(userId)
                    .orElseThrow(()-> new NotFoundUserException("no exist User!!"));
            Alarm alarm = alarmRepository.findById(id)
                    .orElseThrow(() ->  new NotFoundAlarmException("no exist alarm!!"));
            alarm.setChecked(true);
            return CheckAlarmResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
    public DeleteAlarmResponse deleteAlarm(Long id, Long userId) {

            userRepository.findById(userId)
                    .orElseThrow(()-> new NotFoundUserException("no exist User!!"));
            Alarm alarm = alarmRepository
                    .findById(id).orElseThrow(() ->  new NotFoundAlarmException("no exist alarm!!"));
            alarmRepository.delete(alarm);
            return DeleteAlarmResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
    public AlarmResponses alarmList(Integer page, Long userId, Boolean checked) {

            User user = userRepository.findById(userId)
                    .orElseThrow(()-> new NotFoundUserException("no exist User!!"));
            return toAlarmResponses(page-1,user,checked);
    }
    private AlarmResponses toAlarmResponses(Integer pageNum, User user,boolean checked) {
        Page<Alarm> page = null;
        if(checked){
            PageRequest pageRequest = PageRequest.of(pageNum - 1, 10);
            page = alarmRepository.findCheckedAlarmPage(pageRequest, user.getId());
        }else{
            PageRequest pageRequest = PageRequest.of(pageNum - 1, 10);
            page = alarmRepository.findUncheckedAlarmPage(pageRequest, user.getId());
        }
        boolean hasNext = page.hasNext();
        List<AlarmElement> content = page.map(AlarmElement::from).getContent();
        return AlarmResponses.of(SUCCESS_CODE,SUCCESS_MESSAGE,content,hasNext);

    }

    public void save(Alarm alarm) {
        alarmRepository.save(alarm);
    }

    public void saveAll(List<Alarm> list) {
        alarmRepository.saveAll(list);
    }
}
