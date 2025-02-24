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

import static spring.myproject.util.ConstClass.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendService {

    private final UserRepository userRepository;
    private final AttendRepository attendRepository;
    private final MeetingRepository meetingRepository;
    public AddAttendResponse addAttend(Long meetingId, String username) {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeeting("no exist Meeting!!"));
            if(meeting.getCreatedBy().getId()  == meetingId) throw new AutoAttendException("Meeting Opener auto attend");
            Attend checkAttend = attendRepository.findByUserIdAndMeetingId(user.getId(),meetingId);
            if(checkAttend != null) throw new AlreadyAttendExeption("Meeting already attend");
            Attend attend = Attend.of(false,meeting,user,LocalDateTime.now());
            attendRepository.save(attend);
            return AddAttendResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public DisAttendResponse disAttend(Long meetingId, Long attendId, String username) {

            userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeeting("no exist Meeting!!"));
            Attend attend = attendRepository.findById(attendId).orElseThrow(() -> {
                throw new AlreadyAttendExeption("Already Attend Meeting!!");
            });
            if(meeting.getCreatedBy().getId()  == meetingId){
                throw new NotWithdrawException("Opener cannot  disAttend Meeting!!");
            }
            attendRepository.delete(attend);
            return DisAttendResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public PermitAttendResponse permitAttend(Long meetingId, Long attendId, String username) {

            userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeeting("no exist Meeting!!"));
            Attend attend = attendRepository.findById(attendId).orElseThrow(() -> new NotFoundAttend("no exist Attend!!"));
            if(attend.getAccepted() == true) throw new AlreadyAttendExeption("already attend!!");
            if(meeting.getCreatedBy().getId()  == meetingId) throw new AlwaysPermitException("opener always permitted");
            attend.changeAccepted(true);
            return PermitAttendResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
}
