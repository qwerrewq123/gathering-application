package spring.myproject.service.attend;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.entity.attend.Attend;
import spring.myproject.common.exception.attend.AlreadyAttendExeption;
import spring.myproject.common.exception.attend.NotFoundAttendException;
import spring.myproject.common.exception.attend.NotWithdrawException;
import spring.myproject.repository.attend.AttendRepository;
import spring.myproject.entity.meeting.Meeting;
import spring.myproject.common.exception.meeting.NotAuthorizeException;
import spring.myproject.repository.meeting.MeetingRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.common.exception.meeting.NotFoundMeetingExeption;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.service.recommend.RecommendService;

import java.time.LocalDateTime;
import java.util.List;

import static spring.myproject.dto.response.attend.AttendResponseDto.*;
import static spring.myproject.utils.ConstClass.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendService {

        private final UserRepository userRepository;
        private final AttendRepository attendRepository;
        private final MeetingRepository meetingRepository;
        private final RecommendService recommendService;
        public AddAttendResponse addAttend(Long meetingId, Long userId,Long gatheringId) {

                User user = userRepository.findById(userId).orElseThrow(()->new NotFoundUserException("no exist User!!"));
                Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeetingExeption("no exist Meeting!!"));
                Attend checkAttend = attendRepository.findByUserIdAndMeetingId(user.getId(),meetingId);
                if(checkAttend != null) throw new AlreadyAttendExeption("Meeting already attend");
                Attend attend = Attend.of(false,meeting,user,LocalDateTime.now());
                attendRepository.save(attend);
                return AddAttendResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
        }

        public DisAttendResponse disAttend(Long meetingId, Long attendId, Long userId,Long gatheringId) {

                userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("no exist User!!"));
                Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeetingExeption("no exist Meeting!!"));
                Attend attend = attendRepository.findById(attendId).orElseThrow(() -> new NotFoundAttendException("Not Found Attend!!"));
                Long createdById = meeting.getCreatedBy().getId();
                Long attendById = attend.getAttendBy().getId();
                if(!attendById.equals(userId)) throw new NotAuthorizeException("Not Authorized");
                checkMeetingOpener(createdById, userId, meeting, attend);
                recommendService.addScore(gatheringId,-1);
                return DisAttendResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
        }

        public PermitAttendResponse permitAttend(Long meetingId, Long attendId, Long userId) {
                userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("no exist User!!"));
                Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeetingExeption("no exist Meeting!!"));
                Attend attend = attendRepository.findByIdAndAccepted(attendId,false).orElseThrow(() -> new NotFoundAttendException("no exist Attend!!"));
                Long createdBy = meeting.getCreatedBy().getId();
                if(!createdBy.equals(userId)) throw new NotAuthorizeException("this user has no permission");
                attend.changeAccepted(true);
                meeting.changeCount(meeting.getCount()+1);
                return PermitAttendResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
        }

        private void checkMeetingOpener(Long createdById, Long userId, Meeting meeting, Attend attend) {
            if(createdById.equals(userId)){
                    meetingRepository.delete(meeting);
            }else{
                attendRepository.delete(attend);
            }
            if(attend.getAccepted()) meeting.changeCount(meeting.getCount()-1);
        }
}
