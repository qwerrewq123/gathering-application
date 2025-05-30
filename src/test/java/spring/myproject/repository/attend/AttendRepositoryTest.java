package spring.myproject.repository.attend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.entity.attend.Attend;
import spring.myproject.entity.category.Category;
import spring.myproject.repository.category.CategoryRepository;
import spring.myproject.entity.enrollment.Enrollment;
import spring.myproject.repository.enrollment.EnrollmentRepository;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.meeting.Meeting;
import spring.myproject.repository.meeting.MeetingRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.utils.DummyData.*;
import static spring.myproject.utils.DummyData.returnDummyAttend;

@SpringBootTest
@Transactional
class AttendRepositoryTest {
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
    @Test
    void findByUserIdAndMeetingId() {
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
        categoryRepository.save(category);
        userRepository.saveAll(List.of(user1,user2,user3));
        gatheringRepository.saveAll(List.of(gathering));
        enrollmentRepository.saveAll(List.of(enrollment1,enrollment2,enrollment3));
        meetingRepository.saveAll(List.of(meeting));
        attendRepository.saveAll(List.of(attend1,attend2,attend3));

        Attend fetchAttend1 = attendRepository.findByUserIdAndMeetingId(user1.getId(), meeting.getId());
        assertThat(fetchAttend1).isNotNull();
        assertThat(fetchAttend1).extracting("attendBy").isEqualTo(user1);
        Attend fetchAttend2 = attendRepository.findByUserIdAndMeetingId(user2.getId(), meeting.getId());
        assertThat(fetchAttend2).isNotNull();
        assertThat(fetchAttend2).extracting("attendBy").isEqualTo(user2);
        Attend fetchAttend3 = attendRepository.findByUserIdAndMeetingId(user3.getId(), meeting.getId());
        assertThat(fetchAttend3).isNotNull();
        assertThat(fetchAttend3).extracting("attendBy").isEqualTo(user3);
    }
}