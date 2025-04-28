package spring.myproject.repository.meeting;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.common.exception.meeting.NotFoundMeetingExeption;
import spring.myproject.dto.response.meeting.querydto.MeetingsQueryInterface;
import spring.myproject.entity.attend.Attend;
import spring.myproject.repository.attend.AttendRepository;
import spring.myproject.entity.category.Category;
import spring.myproject.repository.category.CategoryRepository;
import spring.myproject.entity.enrollment.Enrollment;
import spring.myproject.repository.enrollment.EnrollmentRepository;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.meeting.Meeting;
import spring.myproject.dto.response.meeting.querydto.MeetingDetailQuery;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.utils.DummyData.*;
import static spring.myproject.utils.DummyData.returnDummyEnrollment;

@SpringBootTest
@Transactional
class MeetingRepositoryTest {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GatheringRepository gatheringRepository;
    @Autowired
    EnrollmentRepository enrollmentRepository;
    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    AttendRepository attendRepository;
    @Autowired
    EntityManager em;

    @Test
    void meetingDetail(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        Image meetingImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        User user2 = returnDummyUser(2, userImage);
        User user3 = returnDummyUser(3, userImage);
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage);
        Enrollment enrollment1 = returnDummyEnrollment(user1,gathering);
        Enrollment enrollment2 = returnDummyEnrollment(user2,gathering);
        Enrollment enrollment3 = returnDummyEnrollment(user3,gathering);
        gathering.enroll(List.of(enrollment1,enrollment2,enrollment3));
        Meeting meeting = returnDummyMeeting(1, user1, gathering,meetingImage);
        Attend attend1 = returnDummyAttend(user1,meeting);
        Attend attend2 = returnDummyAttend(user2,meeting);
        Attend attend3 = returnDummyAttend(user3,meeting);
        meeting.attend(List.of(attend1,attend2,attend3));
        imageRepository.saveAll(List.of(userImage,gatheringImage,meetingImage));
        userRepository.saveAll(List.of(user1,user2,user3));
        categoryRepository.save(category);
        gatheringRepository.saveAll(List.of(gathering));
        enrollmentRepository.saveAll(List.of(enrollment1,enrollment2,enrollment3));
        meetingRepository.save(meeting);
        attendRepository.saveAll(List.of(attend1,attend2,attend3));

        List<MeetingDetailQuery> meetingDetailQueries = meetingRepository.meetingDetail(meeting.getId());
        assertThat(meetingDetailQueries).hasSize(3);
        assertThat(meetingDetailQueries).extracting("attendedBy")
                .containsExactly(
                        "user1","user2","user3"
                );
    }
    @Test
    void meetings(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        Image meetingImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        User user2 = returnDummyUser(2, userImage);
        User user3 = returnDummyUser(3, userImage);
        User user4 = returnDummyUser(4, userImage);
        User user5 = returnDummyUser(5, userImage);
        User user6 = returnDummyUser(6, userImage);
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage);
        Enrollment enrollment1 = returnDummyEnrollment(user1,gathering);
        Enrollment enrollment2 = returnDummyEnrollment(user2,gathering);
        Enrollment enrollment3 = returnDummyEnrollment(user3,gathering);
        Enrollment enrollment4 = returnDummyEnrollment(user4,gathering);
        Enrollment enrollment5 = returnDummyEnrollment(user5,gathering);
        Enrollment enrollment6 = returnDummyEnrollment(user6,gathering);
        gathering.enroll(List.of(enrollment1,enrollment2,enrollment3,enrollment4,enrollment5,enrollment6));
        Meeting meeting1 = returnDummyMeeting(1, user1, gathering,meetingImage);
        Meeting meeting2 = returnDummyMeeting(2, user4, gathering,meetingImage);
        Attend attend1 = returnDummyAttend(user1,meeting1);
        Attend attend2 = returnDummyAttend(user2,meeting1);
        Attend attend3 = returnDummyAttend(user3,meeting1);
        Attend attend4 = returnDummyAttend(user4,meeting2);
        Attend attend5 = returnDummyAttend(user5,meeting2);
        Attend attend6 = returnDummyAttend(user6,meeting2);
        meeting1.attend(List.of(attend1,attend2,attend3));
        meeting2.attend(List.of(attend4,attend5,attend6));
        categoryRepository.save(category);
        userRepository.saveAll(List.of(user1,user2,user3,user4,user5,user6));
        imageRepository.saveAll(List.of(userImage,gatheringImage,meetingImage));
        gatheringRepository.saveAll(List.of(gathering));
        enrollmentRepository.saveAll(List.of(enrollment1,enrollment2,enrollment3,enrollment4,enrollment5,enrollment6));
        meetingRepository.saveAll(List.of(meeting1,meeting2));
        attendRepository.saveAll(List.of(attend1,attend2,attend3,attend4,attend5,attend6));
        List<MeetingsQueryInterface> meetings = meetingRepository.meetings(0, gathering.getId());
        assertThat(meetings).hasSize(6);
    }
    @Test
    void updateCount(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        Image meetingImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        User user2 = returnDummyUser(2, userImage);
        User user3 = returnDummyUser(3, userImage);
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage);
        Enrollment enrollment1 = returnDummyEnrollment(user1,gathering);
        Enrollment enrollment2 = returnDummyEnrollment(user2,gathering);
        Enrollment enrollment3 = returnDummyEnrollment(user3,gathering);
        gathering.enroll(List.of(enrollment1,enrollment2,enrollment3));
        Meeting meeting = returnDummyMeeting(1, user1, gathering,meetingImage);
        Attend attend1 = returnDummyAttend(user1,meeting);
        Attend attend2 = returnDummyAttend(user2,meeting);
        Attend attend3 = returnDummyAttend(user3,meeting);
        meeting.attend(List.of(attend1,attend2,attend3));
        imageRepository.saveAll(List.of(userImage,gatheringImage,meetingImage));
        userRepository.saveAll(List.of(user1,user2,user3));
        categoryRepository.save(category);
        gatheringRepository.saveAll(List.of(gathering));
        enrollmentRepository.saveAll(List.of(enrollment1,enrollment2,enrollment3));
        meetingRepository.save(meeting);
        attendRepository.saveAll(List.of(attend1,attend2,attend3));
        meetingRepository.updateCount(meeting.getId(),10);
        em.flush();
        em.clear();
        Meeting fetchMeeting = meetingRepository.findById(meeting.getId())
                .orElseThrow(() -> new NotFoundMeetingExeption("Not Found Meeting!"));

        assertThat(fetchMeeting).extracting("count").isEqualTo(11);
    }

}