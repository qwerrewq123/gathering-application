package spring.myproject.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.domain.alarm.Alarm;
import spring.myproject.domain.alarm.repository.AlarmRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;
import spring.myproject.dto.request.alarm.AddAlarmRequest;
import spring.myproject.dto.response.alarm.AlarmResponse;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Transactional
public class AlarmService {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    public void checkAlarm(Long id,String username) {

        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("해당하는 유저가 없습니다");
        }
        Alarm alarm = alarmRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException("해당하는 알림이 없습니다");
        });


        alarm.setChecked(true);


    }

    public void deleteAlarm(Long id, String username) {


        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("해당하는 유저가 없습니다");
        }
        Alarm alarm = alarmRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException("해당하는 알림이 없습니다");
        });


        alarmRepository.delete(alarm);



    }

    public Page<AlarmResponse> alarmList(Integer page, String username,Boolean checked) {

        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("해당하는 유저가 없습니다");
        }

        if(checked == true){
            PageRequest pageRequest = PageRequest.of(page - 1, 10);
            Page<Alarm> alarmPage = alarmRepository.findCheckedAlarmPage(pageRequest, user.getId());
            Page<AlarmResponse> alarmResponsePage = alarmPage.map(a -> AlarmResponse.builder()
                    .date(a.getDate())
                    .content(a.getContent())
                    .checked(a.getChecked())
                    .build());

            return alarmResponsePage;
        }else{
            PageRequest pageRequest = PageRequest.of(page - 1, 10);
            Page<Alarm> alarmPage = alarmRepository.findUncheckedAlarmPage(pageRequest, user.getId());
            Page<AlarmResponse> alarmResponsePage = alarmPage.map(a -> AlarmResponse.builder()
                    .date(a.getDate())
                    .content(a.getContent())
                    .checked(a.getChecked())
                    .build());

            return alarmResponsePage;
        }







    }
    public void addAlarm(AddAlarmRequest addAlarmRequest, String username) {

        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("해당하는 유저가 없습니다");
        }


        Alarm alarm = Alarm.builder()
                .date(LocalDateTime.now())
                .content(addAlarmRequest.getContent())
                .checked(false)
                .user(user)
                .build();

        alarmRepository.save(alarm);




    }
}
