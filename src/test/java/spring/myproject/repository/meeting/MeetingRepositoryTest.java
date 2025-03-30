package spring.myproject.entity.meeting.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import spring.myproject.dto.response.meeting.MeetingsQuery;
import spring.myproject.dto.response.meeting.MeetingDetailQuery;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.repository.meeting.MeetingRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.utils.DummyData.*;
import static spring.myproject.utils.DummyData.returnDummyEnrollment;

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

    @Test
    void meetingDetail(){
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
        meetingRepository.save(meeting);
        attendRepository.saveAll(List.of(attend1,attend2));

        List<MeetingDetailQuery> meetingDetailQueries = meetingRepository.meetingDetail(meeting.getId());
        assertThat(meetingDetailQueries).hasSize(2);
        assertThat(meetingDetailQueries).extracting("attendBy")
                .containsExactly(
                        tuple("user2"),
                        tuple("user3")
                );
    }
    @Test
    void meetings(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        User user2 = returnDummyUser(2, userImage);
        User user3 = returnDummyUser(3, userImage);
        User user4 = returnDummyUser(3, userImage);
        User user5 = returnDummyUser(3, userImage);
        User user6 = returnDummyUser(3, userImage);
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage);
        Enrollment enrollment1 = returnDummyEnrollment(user2,gathering);
        Enrollment enrollment2 = returnDummyEnrollment(user3,gathering);
        Enrollment enrollment3 = returnDummyEnrollment(user4,gathering);
        Enrollment enrollment4 = returnDummyEnrollment(user5,gathering);
        Enrollment enrollment5 = returnDummyEnrollment(user6,gathering);
        gathering.enroll(List.of(enrollment1,enrollment2));
        Meeting meeting1 = returnDummyMeeting(1, user1, gathering);
        Meeting meeting2 = returnDummyMeeting(1, user4, gathering);
        Attend attend1 = returnDummyAttend(user2,meeting1);
        Attend attend2 = returnDummyAttend(user2,meeting1);
        Attend attend3 = returnDummyAttend(user2,meeting2);
        Attend attend4 = returnDummyAttend(user2,meeting2);
        meeting1.attend(List.of(attend1,attend2));
        meeting2.attend(List.of(attend3,attend4));
        categoryRepository.save(category);
        userRepository.saveAll(List.of(user1,user2,user3,user4,user5,user6));
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        gatheringRepository.saveAll(List.of(gathering));
        enrollmentRepository.saveAll(List.of(enrollment1,enrollment2,enrollment3,enrollment4,enrollment5));
        meetingRepository.saveAll(List.of(meeting1,meeting2));
        attendRepository.saveAll(List.of(attend1,attend2,attend3,attend4));
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<MeetingsQuery> meetingsQueries = meetingRepository.meetings(pageRequest, "");
        assertThat(meetingsQueries.getTotalPages()).isEqualTo(2);
        assertThat(meetingsQueries.getTotalElements()).isEqualTo(2);
    }
}