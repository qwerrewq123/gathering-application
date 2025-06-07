package spring.myproject.repository.attend;

import org.junit.jupiter.api.BeforeEach;
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
    Category category;
    Image userImage;
    Image gatheringImage;
    Image meetingImage;
    List<User> users;
    List<Gathering> gatherings;
    List<Enrollment> enrollments;
    List<Meeting> meetings;
    List<Attend> attends;
    @BeforeEach
    void beforeEach(){
        category = returnDummyCategory(1);
        userImage = returnDummyImage(1);
        gatheringImage = returnDummyImage(1);
        meetingImage = returnDummyImage(1);
        users = List.of(returnDummyUser(1, userImage),
                returnDummyUser(2, userImage),
                returnDummyUser(3, userImage),
                returnDummyUser(4, userImage),
                returnDummyUser(5, userImage),
                returnDummyUser(6, userImage));
        gatherings = List.of(returnDummyGathering(1, category, users.get(0), gatheringImage),
                returnDummyGathering(1, category, users.get(0), gatheringImage),
                returnDummyGathering(1, category, users.get(0), gatheringImage),
                returnDummyGathering(1, category, users.get(0), gatheringImage),
                returnDummyGathering(1, category, users.get(0), gatheringImage));
        enrollments = List.of(returnDummyEnrollment(users.get(0),gatherings.get(0)),
                returnDummyEnrollment(users.get(1),gatherings.get(0)),
                returnDummyEnrollment(users.get(2),gatherings.get(0)),
                returnDummyEnrollment(users.get(3),gatherings.get(0)),
                returnDummyEnrollment(users.get(4),gatherings.get(0)),
                returnDummyEnrollment(users.get(5),gatherings.get(0)));
        meetings = List.of(returnDummyMeeting(1, users.get(0), gatherings.get(0),meetingImage),
                returnDummyMeeting(1, users.get(3), gatherings.get(0),meetingImage));
        attends = List.of(returnDummyAttend(users.get(0),meetings.get(0)),
                returnDummyAttend(users.get(1),meetings.get(0)),
                returnDummyAttend(users.get(2),meetings.get(0)),
                returnDummyAttend(users.get(3),meetings.get(1)),
                returnDummyAttend(users.get(4),meetings.get(1)),
                returnDummyAttend(users.get(5),meetings.get(1)));

    }
    @Test
    void findByUserIdAndMeetingId() {
        User user1 = users.get(0);
        User user2 = users.get(1);
        User user3 = users.get(2);
        Meeting meeting = meetings.get(0);
        imageRepository.saveAll(List.of(userImage,gatheringImage,meetingImage));
        categoryRepository.save(category);
        userRepository.saveAll(users);
        gatheringRepository.saveAll(gatherings);
        enrollmentRepository.saveAll(enrollments);
        meetingRepository.saveAll(meetings);
        attendRepository.saveAll(attends);

        Attend fetchAttend1 = attendRepository.findByUserIdAndMeetingId(user1.getId(), meeting.getId());
        Attend fetchAttend2 = attendRepository.findByUserIdAndMeetingId(user2.getId(), meeting.getId());
        Attend fetchAttend3 = attendRepository.findByUserIdAndMeetingId(user3.getId(), meeting.getId());
        assertThat(fetchAttend1).isNotNull()
                .extracting("attendBy").isEqualTo(user1);
        assertThat(fetchAttend2).isNotNull()
                .extracting("attendBy").isEqualTo(user1);
        assertThat(fetchAttend3).isNotNull()
                .extracting("attendBy").isEqualTo(user1);
    }
}