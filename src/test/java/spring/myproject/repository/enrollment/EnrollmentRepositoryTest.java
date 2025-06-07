package spring.myproject.repository.enrollment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.entity.category.Category;
import spring.myproject.repository.category.CategoryRepository;
import spring.myproject.entity.enrollment.Enrollment;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static spring.myproject.utils.DummyData.*;
import static spring.myproject.utils.DummyData.returnDummyEnrollment;

@SpringBootTest
@Transactional
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
    Category category;
    Image userImage;
    Image gatheringImage;
    List<User> users;
    List<Gathering> gatherings;
    List<Enrollment> enrollments;
    @BeforeEach
    void beforeEach(){
        category = returnDummyCategory(1);
        userImage = returnDummyImage(1);
        gatheringImage = returnDummyImage(1);
        users = List.of(returnDummyUser(1, userImage),
                returnDummyUser(2, userImage),
                returnDummyUser(3, userImage));
        gatherings = List.of(returnDummyGathering(1, category, users.get(0), gatheringImage),
                returnDummyGathering(1, category, users.get(0), gatheringImage),
                returnDummyGathering(1, category, users.get(0), gatheringImage),
                returnDummyGathering(1, category, users.get(0), gatheringImage),
                returnDummyGathering(1, category, users.get(0), gatheringImage));
        enrollments = List.of(returnDummyEnrollment(users.get(0),gatherings.get(0)),
                returnDummyEnrollment(users.get(1),gatherings.get(0)),
                returnDummyEnrollment(users.get(2),gatherings.get(0)));
    }
    @Test
    void existEnrollment(){
        Gathering gathering = gatherings.get(0);
        User user1 = users.get(0);
        User user2 = users.get(1);
        User user3 = users.get(2);
        categoryRepository.save(category);
        userRepository.saveAll(users);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        gatheringRepository.saveAll(gatherings);
        enrollmentRepository.saveAll(enrollments);


        Enrollment fetchEnrollment1 = enrollmentRepository.existEnrollment(gathering.getId(), user1.getId());
        Enrollment fetchEnrollment2 = enrollmentRepository.existEnrollment(gathering.getId(), user2.getId());
        Enrollment fetchEnrollment3 = enrollmentRepository.existEnrollment(gathering.getId(), user3.getId());

        assertThat(fetchEnrollment1).isNotNull();
        assertThat(fetchEnrollment1.getEnrolledBy()).isEqualTo(user1);
        assertThat(fetchEnrollment2).isNotNull();
        assertThat(fetchEnrollment2.getEnrolledBy()).isEqualTo(user2);
        assertThat(fetchEnrollment3).isNotNull();
        assertThat(fetchEnrollment3.getEnrolledBy()).isEqualTo(user3);
    }
    @Test
    void findEnrollment(){
        Gathering gathering = gatherings.get(0);
        User user1 = users.get(0);
        User user2 = users.get(1);
        User user3 = users.get(2);
        categoryRepository.save(category);
        userRepository.saveAll(users);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        gatheringRepository.saveAll(gatherings);
        enrollmentRepository.saveAll(enrollments);

        Optional<Enrollment> enrollmentOptional1 = enrollmentRepository.findEnrollment(gathering.getId(), user1.getId(),true);
        Optional<Enrollment> enrollmentOptional2 = enrollmentRepository.findEnrollment(gathering.getId(), user2.getId(),true);
        Optional<Enrollment> enrollmentOptional3 = enrollmentRepository.findEnrollment(gathering.getId(), user3.getId(),true);

        assertThat(enrollmentOptional1.isPresent()).isTrue();
        assertThat(enrollmentOptional1.get()).extracting("enrolledBy").isEqualTo(user1);
        assertThat(enrollmentOptional2.isPresent()).isTrue();
        assertThat(enrollmentOptional2.get()).extracting("enrolledBy").isEqualTo(user2);
        assertThat(enrollmentOptional3.isPresent()).isTrue();
        assertThat(enrollmentOptional3.get()).extracting("enrolledBy").isEqualTo(user3);
    }
    @Test
    void findByGatheringAndEnrolledBy(){
        Gathering gathering = gatherings.get(0);
        User user1 = users.get(0);
        User user2 = users.get(1);
        User user3 = users.get(2);
        categoryRepository.save(category);
        userRepository.saveAll(users);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        gatheringRepository.saveAll(gatherings);
        enrollmentRepository.saveAll(enrollments);

        Optional<Enrollment> optionalEnrollment1 = enrollmentRepository.findByGatheringAndEnrolledBy(gathering, user1);
        Optional<Enrollment> optionalEnrollment2 = enrollmentRepository.findByGatheringAndEnrolledBy(gathering, user2);
        Optional<Enrollment> optionalEnrollment3 = enrollmentRepository.findByGatheringAndEnrolledBy(gathering, user3);

        assertThat(optionalEnrollment1).isPresent();
        assertThat(optionalEnrollment1.get().getEnrolledBy()).isEqualTo(user1);
        assertThat(optionalEnrollment2).isPresent();
        assertThat(optionalEnrollment2.get().getEnrolledBy()).isEqualTo(user2);
        assertThat(optionalEnrollment3).isPresent();
        assertThat(optionalEnrollment3.get().getEnrolledBy()).isEqualTo(user3);
    }




}