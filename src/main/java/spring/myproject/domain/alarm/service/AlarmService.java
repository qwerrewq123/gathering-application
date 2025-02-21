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
import spring.myproject.dto.response.alarm.*;
import spring.myproject.dto.response.attend.AddAttendResponse;
import spring.myproject.exception.alarm.NotFoundAlarmException;
import spring.myproject.exception.user.NotFoundUserException;
import spring.myproject.util.AlarmConst;

import java.time.LocalDateTime;

import static spring.myproject.util.UserConst.*;

@RequiredArgsConstructor
@Service
@Transactional
public class AlarmService {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;


    public CheckAlarmResponse checkAlarm(Long id, String username) {


        try {
            User user = userRepository.findByUsername(username).orElseThrow(()-> new NotFoundUserException("no exist User!!"));
            Alarm alarm = alarmRepository.findById(id).orElseThrow(() ->  new NotFoundAlarmException("no exist alarm!!"));

            alarm.setChecked(true);
            return CheckAlarmResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();


        }catch (NotFoundUserException e){
            return CheckAlarmResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();

        }catch (NotFoundAlarmException e){
            return CheckAlarmResponse.builder()
                    .code(AlarmConst.notFoundCode)
                    .message(AlarmConst.notFoundMessage)
                    .build();
        }catch (Exception e){
            return CheckAlarmResponse.builder()
                    .code(dbErrorCode)
                    .message(dbErrorMessage)
                    .build();
        }


    }

    public DeleteAlarmResponse deleteAlarm(Long id, String username) {




        try {

            User user = userRepository.findByUsername(username).orElseThrow(()-> new NotFoundUserException("no exist User!!"));

            Alarm alarm = alarmRepository.findById(id).orElseThrow(() ->  new IllegalArgumentException("no exist alarm!!"));


            alarmRepository.delete(alarm);

            return DeleteAlarmResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();


        }catch (NotFoundUserException e){
            return DeleteAlarmResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();

        }catch (NotFoundAlarmException e){
            return DeleteAlarmResponse.builder()
                    .code(AlarmConst.notFoundCode)
                    .message(AlarmConst.notFoundMessage)
                    .build();
        }catch (Exception e){
            return DeleteAlarmResponse.builder()
                    .code(dbErrorCode)
                    .message(dbErrorMessage)
                    .build();
        }



    }

    public AlarmResponsePage alarmList(Integer page, String username, Boolean checked) {



        try {

            User user = userRepository.findByUsername(username).orElseThrow(()-> new NotFoundUserException("no exist User!!"));

            if(checked == true){
                PageRequest pageRequest = PageRequest.of(page - 1, 10);
                Page<Alarm> alarmPage = alarmRepository.findCheckedAlarmPage(pageRequest, user.getId());
                Page<AlarmResponse> alarmResponsePage = alarmPage.map(a -> AlarmResponse.builder()
                        .date(a.getDate())
                        .content(a.getContent())
                        .checked(a.getChecked())
                        .build());

                return AlarmResponsePage.builder()
                        .code(successCode)
                        .message(successMessage)
                        .page(alarmResponsePage)
                        .build();
            }else{
                PageRequest pageRequest = PageRequest.of(page - 1, 10);
                Page<Alarm> alarmPage = alarmRepository.findUncheckedAlarmPage(pageRequest, user.getId());
                Page<AlarmResponse> alarmResponsePage = alarmPage.map(a -> AlarmResponse.builder()
                        .date(a.getDate())
                        .content(a.getContent())
                        .checked(a.getChecked())
                        .build());

                return AlarmResponsePage.builder()
                        .code(successCode)
                        .message(successMessage)
                        .page(alarmResponsePage)
                        .build();
            }

        }catch (NotFoundUserException e){
            return AlarmResponsePage.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();

        }catch (Exception e){
            return AlarmResponsePage.builder()
                    .code(dbErrorCode)
                    .message(dbErrorMessage)
                    .build();
        }






    }
    public AddAlarmResponse addAlarm(AddAlarmRequest addAlarmRequest, String username) {



        try {
            User user = userRepository.findByUsername(username).orElseThrow(()-> new NotFoundUserException("no exist User!!"));


            Alarm alarm = Alarm.builder()
                    .date(LocalDateTime.now())
                    .content(addAlarmRequest.getContent())
                    .checked(false)
                    .user(user)
                    .build();

            alarmRepository.save(alarm);
            return AddAlarmResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();


        }catch (NotFoundUserException e){
            return AddAlarmResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();

        }catch (Exception e){
            return AddAlarmResponse.builder()
                    .code(dbErrorCode)
                    .message(dbErrorMessage)
                    .build();
        }




    }
}
