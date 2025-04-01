package spring.myproject.service.attend;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.dto.response.attend.AttendResponseDto;
import spring.myproject.entity.attend.Attend;
import spring.myproject.exception.attend.AlreadyAttendExeption;
import spring.myproject.exception.attend.NotFoundAttendException;
import spring.myproject.exception.attend.NotWithdrawException;
import spring.myproject.repository.attend.AttendRepository;
import spring.myproject.entity.meeting.Meeting;
import spring.myproject.exception.meeting.NotAuthorizeException;
import spring.myproject.repository.meeting.MeetingRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.exception.meeting.NotFoundMeetingExeption;
import spring.myproject.exception.user.NotFoundUserException;

import java.time.LocalDateTime;

import static spring.myproject.dto.response.attend.AttendResponseDto.*;
import static spring.myproject.utils.ConstClass.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendService {

        private final UserRepository userRepository;
        private final AttendRepository attendRepository;
        private final MeetingRepository meetingRepository;
        public AddAttendResponse addAttend(Long meetingId, String username) {

                User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
                Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeetingExeption("no exist Meeting!!"));
                Attend checkAttend = attendRepository.findByUserIdAndMeetingId(user.getId(),meetingId);
                if(checkAttend != null) throw new AlreadyAttendExeption("Meeting already attend");
                Attend attend = Attend.of(false,meeting,user,LocalDateTime.now());
                attendRepository.save(attend);
                return AddAttendResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
        }

        public DisAttendResponse disAttend(Long meetingId, Long attendId, String username) {

                User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundUserException("no exist User!!"));
                Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeetingExeption("no exist Meeting!!"));
                Attend attend = attendRepository.findByIdAndAccepted(attendId,true).orElseThrow(() -> new NotFoundAttendException("Not Found Attend!!"));
                Long createdById = meeting.getCreatedBy().getId();
                Long userId = user.getId();
                checkMeetingOpener(createdById, userId, meeting, attend);
                return DisAttendResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
        }

        public PermitAttendResponse permitAttend(Long meetingId, Long attendId, String username) {
                User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundUserException("no exist User!!"));
                Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeetingExeption("no exist Meeting!!"));
                Attend attend = attendRepository.findById(attendId).orElseThrow(() -> new NotFoundAttendException("no exist Attend!!"));
                Long createdBy = meeting.getCreatedBy().getId();
                Long userId = user.getId();
                if(!createdBy.equals(userId)) throw new NotAuthorizeException("this user has no permission");
                if(attend.getAccepted()) throw new AlreadyAttendExeption("already attend!!");
                meeting.setCount(meeting.getCount()+1);
                attend.changeAccepted(true);
                return PermitAttendResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
        }

        private void checkMeetingOpener(Long createdById, Long userId, Meeting meeting, Attend attend) {
            if(createdById.equals(userId)){
                if(meeting.getCount()>1){
                    throw new NotWithdrawException("Opener can disAttend Meeting when count = 1");
                }else{
                    attendRepository.delete(attend);
                    meetingRepository.delete(meeting);
                }
            }else{
                meeting.changeCount(meeting.getCount()-1);
                attendRepository.delete(attend);
            }
        }
}
