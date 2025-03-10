package spring.myproject.domain.attend.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.myproject.domain.attend.Attend;
import spring.myproject.domain.category.Category;
import spring.myproject.domain.category.repository.CategoryRepository;
import spring.myproject.domain.enrollment.Enrollment;
import spring.myproject.domain.enrollment.repository.EnrollmentRepository;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.gathering.repository.GatheringRepository;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.meeting.Meeting;
import spring.myproject.domain.meeting.repository.MeetingRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static spring.myproject.util.DummyData.*;
import static spring.myproject.util.DummyData.returnDummyAttend;

@SpringBootTest
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
        User user1 = returnDummyUser(1, userImage);
        User user2 = returnDummyUser(2, userImage);
        User user3 = returnDummyUser(3, userImage);
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage);
        Enrollment enrollment1 = returnDummyEnrollment(user2,gathering);
        Enrollment enrollment2 = returnDummyEnrollment(user3,gathering);
        gathering.enroll(List.of(enrollment1,enrollment2));
        Meeting meeting = returnDummyMeeting(1, user1, gathering);
        Attend attend1 = returnDummyAttend(user2,meeting);
        Attend attend2 = returnDummyAttend(user2,meeting);
        meeting.attend(List.of(attend1,attend2));
        categoryRepository.save(category);
        userRepository.saveAll(List.of(user1,user2,user3));
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        gatheringRepository.saveAll(List.of(gathering));
        enrollmentRepository.saveAll(List.of(enrollment1,enrollment2));
        meetingRepository.saveAll(List.of(meeting));
        attendRepository.saveAll(List.of(attend1,attend2));

        Attend attend = attendRepository.findByUserIdAndMeetingId(user2.getId(), meeting.getId());
        assertThat(attend).isNotNull();
        assertThat(attend).extracting("attendBy").isEqualTo(user2);
    }

    @Test
    void findByUserIdAndMeetingIdAndTrue() {
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        User user2 = returnDummyUser(2, userImage);
        User user3 = returnDummyUser(3, userImage);
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage);
        Enrollment enrollment1 = returnDummyEnrollment(user2,gathering);
        Enrollment enrollment2 = returnDummyEnrollment(user3,gathering);
        gathering.enroll(List.of(enrollment1,enrollment2));
        Meeting meeting = returnDummyMeeting(1, user1, gathering);
        Attend attend1 = returnDummyAttend(user2,meeting);
        attend1.changeAccepted(true);
        Attend attend2 = returnDummyAttend(user2,meeting);
        meeting.attend(List.of(attend1,attend2));
        categoryRepository.save(category);
        userRepository.saveAll(List.of(user1,user2,user3));
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        gatheringRepository.saveAll(List.of(gathering));
        enrollmentRepository.saveAll(List.of(enrollment1,enrollment2));
        meetingRepository.saveAll(List.of(meeting));
        attendRepository.saveAll(List.of(attend1,attend2));

        Attend fetchAttend1 = attendRepository.findByUserIdAndMeetingIdAndTrue(user2.getId(), meeting.getId());
        Attend fetchAttend2 = attendRepository.findByUserIdAndMeetingIdAndTrue(user3.getId(), meeting.getId());
        assertThat(fetchAttend1).isNotNull();
        assertThat(fetchAttend2).isNull();
    }

    @Test
    void findByIdAndAccepted() {
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        User user2 = returnDummyUser(2, userImage);
        User user3 = returnDummyUser(3, userImage);
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage);
        Enrollment enrollment1 = returnDummyEnrollment(user2,gathering);
        Enrollment enrollment2 = returnDummyEnrollment(user3,gathering);
        gathering.enroll(List.of(enrollment1,enrollment2));
        Meeting meeting = returnDummyMeeting(1, user1, gathering);
        Attend attend1 = returnDummyAttend(user2,meeting);
        attend1.changeAccepted(true);
        Attend attend2 = returnDummyAttend(user2,meeting);
        meeting.attend(List.of(attend1,attend2));
        categoryRepository.save(category);
        userRepository.saveAll(List.of(user1,user2,user3));
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        gatheringRepository.saveAll(List.of(gathering));
        enrollmentRepository.saveAll(List.of(enrollment1,enrollment2));
        meetingRepository.saveAll(List.of(meeting));
        attendRepository.saveAll(List.of(attend1,attend2));

        Optional<Attend> optionalAttend1 = attendRepository.findByIdAndAccepted(attend1.getId(), true);
        Optional<Attend> optionalAttend2 = attendRepository.findByIdAndAccepted(attend2.getId(), false);

        assertThat(optionalAttend1).isPresent();
        assertThat(optionalAttend1.get()).extracting("attendBy").isEqualTo(user2);
        assertThat(optionalAttend2).isEmpty();
    }
}