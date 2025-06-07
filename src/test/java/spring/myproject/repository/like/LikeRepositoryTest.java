package spring.myproject.repository.like;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.entity.category.Category;
import spring.myproject.entity.enrollment.Enrollment;
import spring.myproject.repository.category.CategoryRepository;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.repository.enrollment.EnrollmentRepository;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.like.Like;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.utils.DummyData.*;

@SpringBootTest
@Transactional
class LikeRepositoryTest {
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
    LikeRepository likeRepository;
    Category category;
    Image userImage;
    Image gatheringImage;
    List<User> users;
    List<Gathering> gatherings;
    List<Enrollment> enrollments;
    List<Like> likes;
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
        likes = List.of(returnDummyLike(users.get(1), gatherings.get(0)),
                returnDummyLike(users.get(2), gatherings.get(0)));
    }
    @Test
    void findLike(){
        Gathering gathering = gatherings.get(0);
        User user = users.get(1);
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(users);
        gatheringRepository.saveAll(gatherings);
        enrollmentRepository.saveAll(enrollments);
        likeRepository.saveAll(likes);

        Optional<Like> optionalLike = likeRepository.findLike(user.getId(), gathering.getId());

        assertThat(optionalLike).isPresent();
        assertThat(optionalLike.get()).extracting("likedBy").isEqualTo(user);
    }
}