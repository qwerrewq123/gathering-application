package spring.myproject.repository.meeting;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;

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
    void meetingDetail(){
        Meeting meeting = meetings.get(0);
        imageRepository.saveAll(List.of(userImage,gatheringImage,meetingImage));
        userRepository.saveAll(users);
        categoryRepository.save(category);
        gatheringRepository.saveAll(gatherings);
        enrollmentRepository.saveAll(enrollments);
        meetingRepository.saveAll(meetings);
        attendRepository.saveAll(attends);

        List<MeetingDetailQuery> meetingDetailQueries = meetingRepository.meetingDetail(meeting.getId());
        assertThat(meetingDetailQueries).hasSize(3);
        assertThat(meetingDetailQueries).extracting("attendedBy")
                .containsExactly(
                        "user1","user2","user3"
                );
    }
    @Test
    void meetings(){
        Gathering gathering = gatherings.get(0);
        imageRepository.saveAll(List.of(userImage,gatheringImage,meetingImage));
        userRepository.saveAll(users);
        categoryRepository.save(category);
        gatheringRepository.saveAll(gatherings);
        enrollmentRepository.saveAll(enrollments);
        meetingRepository.saveAll(meetings);
        attendRepository.saveAll(attends);
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
        meetingRepository.updateCount(meeting.getId(),10);
        em.flush();
        em.clear();
        Optional<Meeting> optionalMeeting = meetingRepository.findById(meeting.getId());

        assertThat(optionalMeeting).isPresent()
                .get().extracting("count").isEqualTo(11);
    }

}