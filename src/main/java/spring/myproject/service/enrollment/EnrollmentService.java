package spring.myproject.service.enrollment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.dto.request.fcm.TopicNotificationRequestDto;
import spring.myproject.dto.response.enrollment.EnrollResponseDto;
import spring.myproject.entity.enrollment.Enrollment;
import spring.myproject.entity.fcm.Topic;
import spring.myproject.repository.enrollment.EnrollmentRepository;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.exception.enrollment.AlreadyEnrollmentException;
import spring.myproject.exception.enrollment.NotFoundEnrollmentException;
import spring.myproject.exception.gathering.NotFoundGatheringException;
import spring.myproject.exception.user.NotFoundUserException;
import spring.myproject.service.fcm.FCMService;
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
    private final FCMService fcmService;
    private final RecommendService recommendService;
    public EnrollGatheringResponse enrollGathering(Long gatheringId, String username) {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(
                    () -> new NotFoundGatheringException("no exist Gathering!!"));
            Enrollment exist = enrollmentRepository.existEnrollment(gatheringId, user.getId());
            if(exist != null) throw new AlreadyEnrollmentException("Already enrolled;");
            gathering.changeCount(gathering.getCount()+1);
            Enrollment enrollment = Enrollment.of(true,gathering,user,LocalDateTime.now());
            enrollmentRepository.save(enrollment);
            Topic topic = gathering.getTopic();
            String topicName = topic.getTopicName();
            fcmService.sendByTopic(TopicNotificationRequestDto.builder()
                            .topic(topicName)
                            .title("enrollment")
                            .content("%s enrolled".formatted(username))
                            .url("localhost:8080/gathering/"+gatheringId)
                            .img(null)
                    .build(),topic);
            fcmService.subscribeToTopics(topicName,username);
            recommendService.addScore(gathering,1);
            return EnrollGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
    public DisEnrollGatheringResponse disEnrollGathering(Long gatheringId, String username) {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(
                () -> new NotFoundGatheringException("no exist Gathering!!"));
            Enrollment enrollment = enrollmentRepository.findEnrollment(gatheringId, user.getId()).orElseThrow(
                    () ->  new NotFoundEnrollmentException("no exist Enrollment!!"));
            gathering.changeCount(gathering.getCount()-1);
            enrollmentRepository.delete(enrollment);
            Topic topic = gathering.getTopic();
            String topicName = topic.getTopicName();
            fcmService.sendByTopic(TopicNotificationRequestDto.builder()
                    .topic(topicName)
                    .title("disEnrollment")
                    .content("%s disErolled".formatted(username))
                    .url("localhost:8080/gathering/"+gatheringId)
                    .img(null)
                    .build(),topic);
            fcmService.unsubscribeFromTopics(topicName,username);
            recommendService.addScore(gathering,-1);
            return DisEnrollGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
}
