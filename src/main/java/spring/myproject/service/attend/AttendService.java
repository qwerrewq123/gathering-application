package spring.myproject.service.attend;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import spring.myproject.common.async.AsyncService;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.dto.request.fcm.TopicNotificationRequestDto;
import spring.myproject.entity.attend.Attend;
import spring.myproject.common.exception.attend.AlreadyAttendExeption;
import spring.myproject.common.exception.attend.NotFoundAttendException;
import spring.myproject.common.exception.attend.NotWithdrawException;
import spring.myproject.entity.fcm.Topic;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.repository.attend.AttendRepository;
import spring.myproject.entity.meeting.Meeting;
import spring.myproject.common.exception.meeting.NotAuthorizeException;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.repository.meeting.MeetingRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.common.exception.meeting.NotFoundMeetingExeption;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.service.recommend.RecommendService;

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
        private final GatheringRepository gatheringRepository;
        private final RecommendService recommendService;
        private final AsyncService asyncService;

        public AddAttendResponse addAttend(Long meetingId, Long userId,Long gatheringId) {
                User user = userRepository.findById(userId)
                        .orElseThrow(()->new NotFoundUserException("no exist User!!"));
                Gathering gathering = gatheringRepository.findTopicById(gatheringId)
                        .orElseThrow(() -> new NotFoundGatheringException("no exist Gathering !!"));
                Meeting meeting = meetingRepository.findById(meetingId)
                        .orElseThrow(()->new NotFoundMeetingExeption("no exist Meeting!!"));
                Attend checkAttend = attendRepository.findByUserIdAndMeetingId(user.getId(),meetingId);
                if(checkAttend != null) throw new AlreadyAttendExeption("Meeting already attend");
                Attend attend = Attend.of(meeting,user,LocalDateTime.now());
                attendRepository.save(attend);
                meetingRepository.updateCount(meetingId,1);
                Topic topic = gathering.getTopic();
                String title = "Board created";
                String content = "%s has created board".formatted(user.getNickname());
                TopicNotificationRequestDto topicNotificationRequestDto = TopicNotificationRequestDto.from(title,content,topic);
                asyncService.sendTopic(topicNotificationRequestDto);
                return AddAttendResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
        }

        public DisAttendResponse disAttend(Long meetingId, Long userId,Long gatheringId) {

                userRepository.findById(userId)
                        .orElseThrow(() -> new NotFoundUserException("no exist User!!"));
                Meeting meeting = meetingRepository.findById(meetingId)
                        .orElseThrow(()->new NotFoundMeetingExeption("no exist Meeting!!"));
                Attend attend = attendRepository.findByUserIdAndMeetingId(userId,meetingId);
                if(attend == null) throw  new NotFoundAttendException("Not Found Attend!!");
                Long createdById = meeting.getCreatedBy().getId();
                checkMeetingOpener(createdById, userId, meetingId, attend);
                recommendService.addScore(gatheringId,-1);
                return DisAttendResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
        }


        private void checkMeetingOpener(Long createdById, Long userId, Long meetingId, Attend attend) {
            if(!createdById.equals(userId)){
                    attendRepository.delete(attend);
                    meetingRepository.updateCount(meetingId,1);
            }else{
                throw new NotWithdrawException("cannot withdraw meeting");
            }
        }
}
