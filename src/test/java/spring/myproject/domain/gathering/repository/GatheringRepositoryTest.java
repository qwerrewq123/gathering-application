package spring.myproject.domain.gathering.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import spring.myproject.domain.category.Category;
import spring.myproject.domain.category.repository.CategoryRepository;
import spring.myproject.domain.enrollment.Enrollment;
import spring.myproject.domain.enrollment.repository.EnrollmentRepository;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.gathering.dto.response.GatheringsQuery;
import spring.myproject.domain.gathering.dto.response.GatheringDetailQuery;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.util.DummyData.*;

@SpringBootTest
public class GatheringRepositoryTest {
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
    EntityManager em;
    @Test
    void findPaging(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        User user = returnDummyUser(1, userImage);
        Gathering gathering1 = returnDummyGathering(1, category, user, gatheringImage);
        Gathering gathering2 = returnDummyGathering(1, category, user, gatheringImage);
        Gathering gathering3 = returnDummyGathering(1, category, user, gatheringImage);
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.save(user);
        gatheringRepository.saveAll(List.of(gathering1,gathering2,gathering3));
        em.flush();

        Page<GatheringsQuery> page = gatheringRepository.findPaging(PageRequest.of(0, 10), "title");

        assertThat(page.getTotalElements()).isEqualTo(3);
    }

    @Test
    void findLikePaging(){

    }

    @Test
    void findRecommendPaging(){

    }

    @Test
    void findGatheringAndCount(){
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
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(List.of(user1,user2));
        gatheringCountRepository.save(gatheringCount);
        gatheringRepository.saveAll(List.of(gathering));
        enrollmentRepository.saveAll(List.of(enrollment1,enrollment2));
        em.flush();

        List<GatheringDetailQuery> gatheringAndCount = gatheringRepository.findGatheringAndCount(gathering.getId());

        assertThat(gatheringAndCount.size()).isEqualTo(2);
    }

}
