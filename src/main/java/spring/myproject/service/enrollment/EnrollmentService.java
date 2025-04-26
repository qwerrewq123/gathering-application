package spring.myproject.service.enrollment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.common.exception.enrollment.NotDisEnrollmentException;
import spring.myproject.common.exception.meeting.NotAuthorizeException;
import spring.myproject.dto.request.fcm.TopicNotificationRequestDto;
import spring.myproject.entity.enrollment.Enrollment;
import spring.myproject.entity.fcm.Topic;
import spring.myproject.repository.enrollment.EnrollmentRepository;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.common.exception.enrollment.AlreadyEnrollmentException;
import spring.myproject.common.exception.enrollment.NotFoundEnrollmentException;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.service.fcm.FCMService;
import spring.myproject.service.fcm.FCMTokenTopicService;
import spring.myproject.service.recommend.RecommendService;

import java.time.LocalDateTime;

import static spring.myproject.dto.response.enrollment.EnrollResponseDto.*;
import static spring.myproject.utils.ConstClass.*;

@Service
@Transactional
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final GatheringRepository gatheringRepository;
    private final FCMTokenTopicService fcmTokenTopicService;
    private final RecommendService recommendService;
    public EnrollGatheringResponse enrollGathering(Long gatheringId, Long userId) {
            User user = userRepository.findById(userId)
                    .orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Gathering gathering = gatheringRepository.findTopicById(gatheringId).orElseThrow(
                    () -> new NotFoundGatheringException("no exist Gathering!!"));
            Enrollment exist = enrollmentRepository.existEnrollment(gatheringId, user.getId());
            if(exist != null) throw new AlreadyEnrollmentException("Already enrolled;");
            Enrollment enrollment = Enrollment.of(false,gathering,user,LocalDateTime.now());
            enrollmentRepository.save(enrollment);
            recommendService.addScore(gatheringId,1);
            //todo : 가입 요청(token 소모임 개체자)
            //todo : alarm
            return EnrollGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
    public DisEnrollGatheringResponse disEnrollGathering(Long gatheringId, Long userId) {

            User user = userRepository.findById(userId).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Gathering gathering = gatheringRepository.findTopicById(gatheringId).orElseThrow(
                () -> new NotFoundGatheringException("no exist Gathering!!"));
            Long createdById = gathering.getCreateBy().getId();
            Enrollment enrollment = enrollmentRepository.findEnrollment(gatheringId, user.getId(),true)
                    .orElseThrow(() ->  new NotFoundEnrollmentException("no exist Enrollment!!"));
            if(createdById.equals(userId)) throw new NotDisEnrollmentException("Opener cannot disEnroll!!");
            enrollmentRepository.delete(enrollment);
            gatheringRepository.updatecount(gatheringId,-1);
            Topic topic = gathering.getTopic();
            String topicName = topic.getTopicName();
            fcmTokenTopicService.unsubscribeFromTopic(topicName,userId);
            recommendService.addScore(gatheringId,-1);
            return DisEnrollGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public PermitEnrollmentResponse permit(Long gatheringId, Long userId) {
            userRepository.findById(userId).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(
                    () -> new NotFoundGatheringException("no exist Gathering!!"));
            Enrollment enrollment = enrollmentRepository.findEnrollment(gatheringId, userId, false)
                    .orElseThrow(() -> new NotFoundEnrollmentException("no exist Enrollment!!"));
            Long createdById = gathering.getCreateBy().getId();
            if(!createdById.equals(userId)) throw new NotAuthorizeException("Not authorized");
            enrollment.changeAccepted();
            User enrolledBy = enrollment.getEnrolledBy();
            Long enrolledById = enrolledBy.getId();
            gatheringRepository.updatecount(gatheringId,1);
            Topic topic = gathering.getTopic();
            String topicName = topic.getTopicName();
            fcmTokenTopicService.subscribeToTopic(topicName,enrolledById);
            return PermitEnrollmentResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
            //todo : token 으로 발송하는거(가입승인)
            //todo : alarm
    }
}
