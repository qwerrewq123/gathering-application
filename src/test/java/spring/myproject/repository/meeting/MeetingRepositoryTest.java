package spring.myproject.repository.meeting;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.utils.DummyData.*;
import static spring.myproject.utils.DummyData.returnDummyEnrollment;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.yml")
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
        User user1 = users.get(0);
        User user2 = users.get(1);
        User user3 = users.get(2);
        User user4 = users.get(3);
        User user5 = users.get(4);
        User user6 = users.get(5);
        gatherings = List.of(returnDummyGathering(1, category, user1, gatheringImage),
                returnDummyGathering(2, category, user1, gatheringImage),
                returnDummyGathering(3, category, user1, gatheringImage),
                returnDummyGathering(4, category, user1, gatheringImage),
                returnDummyGathering(5, category, user1, gatheringImage));
        Gathering gathering = gatherings.get(0);
        enrollments = List.of(returnDummyEnrollment(user1,gathering),
                returnDummyEnrollment(user2,gathering),
                returnDummyEnrollment(user3,gathering),
                returnDummyEnrollment(user4,gathering),
                returnDummyEnrollment(user5,gathering),
                returnDummyEnrollment(user6,gathering));
        meetings = List.of(returnDummyMeeting(1, user1, gathering,meetingImage),
                returnDummyMeeting(2, user4, gathering,meetingImage));
        Meeting meeting1 = meetings.get(0);
        Meeting meeting2 = meetings.get(1);
        attends = List.of(returnDummyAttend(user1,meeting1),
                returnDummyAttend(user2,meeting1),
                returnDummyAttend(user3,meeting1),
                returnDummyAttend(user4,meeting2),
                returnDummyAttend(user5,meeting2),
                returnDummyAttend(user6,meeting2));

    }
    @Test
    void meetingDetail(){
        imageRepository.saveAll(List.of(userImage,gatheringImage,meetingImage));
        userRepository.saveAll(users);
        categoryRepository.save(category);
        gatheringRepository.saveAll(gatherings);
        enrollmentRepository.saveAll(enrollments);
        meetingRepository.saveAll(meetings);
        attendRepository.saveAll(attends);
        Meeting meeting = meetings.get(0);

        List<MeetingDetailQuery> meetingDetailQueries = meetingRepository.meetingDetail(meeting.getId());
        assertThat(meetingDetailQueries).hasSize(3);
        assertThat(meetingDetailQueries).extracting("attendedBy")
                .containsExactly(
                        "user1","user2","user3"
                );
    }
    @Test
    void meetings(){
        imageRepository.saveAll(List.of(userImage,gatheringImage,meetingImage));
        userRepository.saveAll(users);
        categoryRepository.save(category);
        gatheringRepository.saveAll(gatherings);
        enrollmentRepository.saveAll(enrollments);
        meetingRepository.saveAll(meetings);
        attendRepository.saveAll(attends);
        Gathering gathering = gatherings.get(0);
        List<MeetingsQueryInterface> meetings = meetingRepository.meetings(0, gathering.getId());
        assertThat(meetings).hasSize(6);
    }
    @Test
    void updateCount(){
        Meeting meeting = meetings.get(0);
        imageRepository.saveAll(List.of(userImage,gatheringImage,meetingImage));
        userRepository.saveAll(users);
        categoryRepository.save(category);
        gatheringRepository.saveAll(gatherings);
        enrollmentRepository.saveAll(enrollments);
        meetingRepository.saveAll(meetings);
        attendRepository.saveAll(attends);
        em.flush();
        em.clear();
        meetingRepository.updateCount(meeting.getId(),10);
        Optional<Meeting> optionalMeeting = meetingRepository.findById(meeting.getId());

        assertThat(optionalMeeting).isPresent()
                .get().extracting("count").isEqualTo(11);
    }

}