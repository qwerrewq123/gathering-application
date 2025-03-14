package spring.myproject.domain.enrollment.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.myproject.domain.Category;
import spring.myproject.repository.category.CategoryRepository;
import spring.myproject.domain.Enrollment;
import spring.myproject.domain.Gathering;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.domain.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.domain.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.repository.enrollment.EnrollmentRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static spring.myproject.util.DummyData.*;
import static spring.myproject.util.DummyData.returnDummyEnrollment;

@SpringBootTest
class EnrollmentRepositoryTest {
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
    @Test
    void existEnrollment(){
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
        categoryRepository.save(category);
        userRepository.saveAll(List.of(user1,user2));
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        gatheringRepository.saveAll(List.of(gathering));
        enrollmentRepository.saveAll(List.of(enrollment1,enrollment2));

        Enrollment fetchEnrollment1 = enrollmentRepository.existEnrollment(gathering.getId(), user2.getId());
        Enrollment fetchEnrollment2 = enrollmentRepository.existEnrollment(gathering.getId(), user3.getId());

        assertThat(fetchEnrollment1).isNotNull();
        assertThat(fetchEnrollment1.getEnrolledBy()).isEqualTo(user2);
        assertThat(fetchEnrollment2).isNotNull();
        assertThat(fetchEnrollment2.getEnrolledBy()).isEqualTo(user3);
    }
    @Test
    void findEnrollment(){
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
        categoryRepository.save(category);
        userRepository.saveAll(List.of(user1,user2));
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        gatheringRepository.saveAll(List.of(gathering));
        enrollmentRepository.saveAll(List.of(enrollment1,enrollment2));

        Optional<Enrollment> enrollmentOptional1 = enrollmentRepository.findEnrollment(gathering.getId(), user2.getId());
        Optional<Enrollment> enrollmentOptional2 = enrollmentRepository.findEnrollment(gathering.getId(), user3.getId());

        assertThat(enrollmentOptional1.isPresent()).isTrue();
        assertThat(enrollmentOptional1.get()).extracting("enrolledBy").isEqualTo(user2);
        assertThat(enrollmentOptional2.isPresent()).isTrue();
        assertThat(enrollmentOptional2.get()).extracting("enrolledBy").isEqualTo(user3);
    }
    @Test
    void findByGatheringAndEnrolledBy(){
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
        categoryRepository.save(category);
        userRepository.saveAll(List.of(user1,user2));
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        gatheringRepository.saveAll(List.of(gathering));
        enrollmentRepository.saveAll(List.of(enrollment1,enrollment2));

        Optional<Enrollment> optionalEnrollment1 = enrollmentRepository.findByGatheringAndEnrolledBy(gathering, user2);
        Optional<Enrollment> optionalEnrollment2 = enrollmentRepository.findByGatheringAndEnrolledBy(gathering, user3);

        assertThat(optionalEnrollment1).isPresent();
        assertThat(optionalEnrollment1.get().getEnrolledBy()).isEqualTo(user2);
        assertThat(optionalEnrollment2).isPresent();
        assertThat(optionalEnrollment2.get().getEnrolledBy()).isEqualTo(user3);
    }




}