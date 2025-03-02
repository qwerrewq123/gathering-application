package spring.myproject.domain.meeting.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import spring.myproject.domain.attend.Attend;
import spring.myproject.domain.attend.repository.AttendRepository;
import spring.myproject.domain.category.Category;
import spring.myproject.domain.category.repository.CategoryRepository;
import spring.myproject.domain.enrollment.Enrollment;
import spring.myproject.domain.enrollment.repository.EnrollmentRepository;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.gathering.repository.GatheringRepository;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.meeting.Meeting;
import spring.myproject.domain.meeting.dto.response.MeetingsQuery;
import spring.myproject.domain.meeting.dto.response.MeetingDetailQuery;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;

import java.util.List;

import static spring.myproject.util.DummyData.*;
import static spring.myproject.util.DummyData.returnDummyEnrollment;

@SpringBootTest
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
    @BeforeEach
    void beforeEach(){

    }
    @Test
    void findAttendsBy(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        User user2 = returnDummyUser(2, userImage);
        User user3 = returnDummyUser(3, userImage);
        GatheringCount gatheringCount = returnDummyGatheringCount();
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage, gatheringCount);
        Enrollment enrollment1 = returnDummyEnrollment(user2);
        Enrollment enrollment2 = returnDummyEnrollment(user3);
        gathering.enroll(List.of(enrollment1,enrollment2));
        Meeting meeting = returnDummyMeeting(1, user1, gathering);
        Attend attend1 = returnDummyAttend(user2);
        Attend attend2 = returnDummyAttend(user2);
        meeting.attend(List.of(attend1,attend2));
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(List.of(user1,user2));
        gatheringCountRepository.save(gatheringCount);
        gatheringRepository.saveAll(List.of(gathering));
        enrollmentRepository.saveAll(List.of(enrollment1,enrollment2));
        meetingRepository.save(meeting);
        attendRepository.saveAll(List.of(attend1,attend2));
        em.flush();

        List<MeetingDetailQuery> attendsBy = meetingRepository.findAttendsBy(meeting.getId());

        Assertions.assertThat(attendsBy.size()).isEqualTo(2 );
    }
    @Test
    void meetings(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        User user2 = returnDummyUser(2, userImage);
        User user3 = returnDummyUser(3, userImage);
        GatheringCount gatheringCount = returnDummyGatheringCount();
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage, gatheringCount);
        Enrollment enrollment1 = returnDummyEnrollment(user2);
        Enrollment enrollment2 = returnDummyEnrollment(user3);
        gathering.enroll(List.of(enrollment1,enrollment2));
        Meeting meeting1 = returnDummyMeeting(1, user1, gathering);
        Meeting meeting2 = returnDummyMeeting(2, user1, gathering);
        Meeting meeting3 = returnDummyMeeting(3, user1, gathering);
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(List.of(user1,user2));
        gatheringCountRepository.save(gatheringCount);
        gatheringRepository.saveAll(List.of(gathering));
        enrollmentRepository.saveAll(List.of(enrollment1,enrollment2));
        meetingRepository.saveAll(List.of(meeting1,meeting2,meeting3));
        em.flush();

        Page<MeetingsQuery> page = meetingRepository.meetings(PageRequest.of(0, 10), "title");

        Assertions.assertThat(page.getTotalElements()).isEqualTo(3);
    }


}