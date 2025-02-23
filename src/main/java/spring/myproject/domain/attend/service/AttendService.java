package spring.myproject.domain.attend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.domain.attend.Attend;
import spring.myproject.domain.attend.exception.*;
import spring.myproject.domain.attend.repository.AttendRepository;
import spring.myproject.domain.meeting.Meeting;
import spring.myproject.domain.meeting.repository.MeetingRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;
import spring.myproject.domain.attend.dto.response.AddAttendResponse;
import spring.myproject.domain.attend.dto.response.DisAttendResponse;
import spring.myproject.domain.attend.dto.response.PermitAttendResponse;
import spring.myproject.domain.meeting.exception.NotFoundMeeting;
import spring.myproject.domain.user.exception.NotFoundUserException;

import java.time.LocalDateTime;

import static spring.myproject.util.UserConst.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendService {

    private final UserRepository userRepository;
    private final AttendRepository attendRepository;
    private final MeetingRepository meetingRepository;
    public AddAttendResponse addAttend(Long meetingId, String username) {


        try {
            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));

            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeeting("no exist Meeting!!"));

            if(meeting.getCreatedBy().getId()  == meetingId){
                throw new AutoAttendException("Meeting Opener auto attend");
            }
            Attend checkAttend = attendRepository.findByUserIdAndMeetingId(user.getId(),meetingId);
            if(checkAttend != null){
                throw new AlreadyAttendExeption("Meeting already attend");
            }

            Attend attend = Attend.builder()
                    .accepted(false)
                    .attendBy(user)
                    .date(LocalDateTime.now())
                    .meeting(meeting)
                    .build();

            attendRepository.save(attend);
            return AddAttendResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();

        }catch (NotFoundUserException e){
            return AddAttendResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();

        }catch (NotFoundMeeting e){
            return AddAttendResponse.builder()
                    .code(MeetingConst.notFoundCode)
                    .message(MeetingConst.notFoundMessage)
                    .build();
        }catch (AutoAttendException e){
            return AddAttendResponse.builder()
                    .code(AttendConst.autoAttendCode)
                    .message(AttendConst.autoAttendMessage)
                    .build();
        }catch (AlreadyAttendExeption e){
            return AddAttendResponse.builder()
                    .code(AttendConst.alreadyAttendCode)
                    .message(AttendConst.alreadyAttendMessage)
                    .build();
        }

    }

    public DisAttendResponse disAttend(Long meetingId, Long attendId, String username) {



        try {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));

            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeeting("no exist Meeting!!"));

            Attend attend = attendRepository.findById(attendId).orElseThrow(() -> {
                throw new AlreadyAttendExeption("Already Attend Meeting!!");
            });

            if(meeting.getCreatedBy().getId()  == meetingId){
                throw new NotWithdrawException("Opener cannot  disAttend Meeting!!");
            }

            attendRepository.delete(attend);
            return DisAttendResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();

        }catch (NotFoundUserException e){
            return DisAttendResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();

        }catch (NotFoundMeeting e){
            return DisAttendResponse.builder()
                    .code(MeetingConst.notFoundCode)
                    .message(MeetingConst.notFoundMessage)
                    .build();
        }catch (AlreadyAttendExeption e){
            return DisAttendResponse.builder()
                    .code(AttendConst.alreadyAttendCode)
                    .message(AttendConst.alreadyAttendMessage)
                    .build();


        }catch (NotWithdrawException e){
            return DisAttendResponse.builder()
                    .code(AttendConst.notWithdrawCode)
                    .message(AttendConst.notWithdrawMessage)
                    .build();

        }catch (Exception e){
            return DisAttendResponse.builder()
                    .code(dbErrorCode)
                    .message(dbErrorMessage)
                    .build();
        }



    }

    public PermitAttendResponse permitAttend(Long meetingId, Long attendId, String username) {

        try {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));

            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeeting("no exist Meeting!!"));

            Attend attend = attendRepository.findById(attendId).orElseThrow(() -> new NotFoundAttend("no exist Attend!!"));

            if(attend.getAccepted() == true){
                throw new AlreadyAttendExeption("already attend!!");
            }

            if(meeting.getCreatedBy().getId()  == meetingId){
                throw new AlwaysPermitException("opener always permitted");

            }

            attend.setAccepted(true);
            return PermitAttendResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();

        }catch (NotFoundUserException e){
        return PermitAttendResponse.builder()
                .code(notFoundCode)
                .message(notFoundMessage)
                .build();

        }catch (NotFoundMeeting e){
        return PermitAttendResponse.builder()
                .code(MeetingConst.notFoundCode)
                .message(MeetingConst.notFoundMessage)
                .build();
        }catch (NotFoundAttend e){
            return PermitAttendResponse.builder()
                    .code(AttendConst.notFoundCode)
                    .message(AttendConst.notFoundMessage)
                    .build();

        }catch (AlreadyAttendExeption e){
            return PermitAttendResponse.builder()
                    .code(AttendConst.alreadyAttendCode)
                    .message(AttendConst.alreadyAttendMessage)
                    .build();
        }catch (AlwaysPermitException e){
            return PermitAttendResponse.builder()
                    .code(AttendConst.alwaysPermitCode)
                    .message(AttendConst.alwaysPermitMessage)
                    .build();
        }


    }
}
