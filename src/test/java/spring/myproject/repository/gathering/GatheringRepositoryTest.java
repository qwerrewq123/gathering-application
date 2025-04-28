package spring.myproject.repository.gathering;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.dto.response.gathering.querydto.GatheringDetailQuery;
import spring.myproject.dto.response.gathering.querydto.GatheringsQuery;
import spring.myproject.entity.category.Category;
import spring.myproject.repository.category.CategoryRepository;
import spring.myproject.entity.enrollment.Enrollment;
import spring.myproject.repository.enrollment.EnrollmentRepository;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.like.Like;
import spring.myproject.repository.like.LikeRepository;
import spring.myproject.repository.recommend.RecommendRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.utils.DummyData.*;

@SpringBootTest
@Transactional
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
    LikeRepository likeRepository;
    @Autowired
    RecommendRepository recommendRepository;
    @Autowired
    EntityManager em;
    @Test
    void gatheringDetail(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        User user2 = returnDummyUser(2, userImage);
        User user3 = returnDummyUser(3, userImage);
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage);
        Enrollment enrollment1 = returnDummyEnrollment(user1,gathering);
        Enrollment enrollment2 = returnDummyEnrollment(user2,gathering);
        Enrollment enrollment3 = returnDummyEnrollment(user3,gathering);
        gathering.enroll(List.of(enrollment1,enrollment2,enrollment3));
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(List.of(user1,user2,user3));
        gatheringRepository.save(gathering);
        enrollmentRepository.saveAll(List.of(enrollment1,enrollment2,enrollment3));

        List<GatheringDetailQuery> gatheringDetailQueries = gatheringRepository.gatheringDetail(gathering.getId());
        assertThat(gatheringDetailQueries).hasSize(3);
        assertThat(gatheringDetailQueries).extracting("participatedBy")
                .containsExactly(
                        "user1","user2","user3"
                );
    }

    @Test
    void gatheringsCategory(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        User user2 = returnDummyUser(2, userImage);
        User user3 = returnDummyUser(3, userImage);
        Gathering gathering1 = returnDummyGathering(1, category, user1, gatheringImage);
        Gathering gathering2 = returnDummyGathering(2, category, user2, gatheringImage);
        Gathering gathering3 = returnDummyGathering(3, category, user3, gatheringImage);

        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(List.of(user1,user2,user3));
        gatheringRepository.saveAll(List.of(gathering1,gathering2,gathering3));

        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<GatheringsQuery> page = gatheringRepository.gatheringsCategory(pageRequest, category.getName());
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
    }
    @Test
    void gatheringsLike(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        User user2 = returnDummyUser(2, userImage);
        User user3 = returnDummyUser(3, userImage);
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage);
        Like like1 = returnDummyLike(user2, gathering);
        Like like2 = returnDummyLike(user3, gathering);
        userRepository.saveAll(List.of(user1,user2,user3));
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        gatheringRepository.save(gathering);
        likeRepository.saveAll(List.of(like1,like2));

        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<GatheringsQuery> gatheringsQueryPage1 = gatheringRepository.gatheringsLike(pageRequest, user2.getId());
        Page<GatheringsQuery> gatheringsQueryPage2 = gatheringRepository.gatheringsLike(pageRequest, user3.getId());
        assertThat(gatheringsQueryPage1.getTotalPages()).isEqualTo(1);
        assertThat(gatheringsQueryPage1.getTotalElements()).isEqualTo(1);
        assertThat(gatheringsQueryPage2.getTotalPages()).isEqualTo(1);
        assertThat(gatheringsQueryPage2.getTotalElements()).isEqualTo(1);
    }
    @Test
    void gatheringsRecommend(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        Gathering gathering1 = returnDummyGathering(1, category, user1, gatheringImage);
        Gathering gathering2 = returnDummyGathering(2, category, user1, gatheringImage);
        Gathering gathering3 = returnDummyGathering(3, category, user1, gatheringImage);
        Gathering gathering4 = returnDummyGathering(4, category, user1, gatheringImage);
        Gathering gathering5 = returnDummyGathering(5, category, user1, gatheringImage);
        userRepository.save(user1);
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        gatheringRepository.saveAll(List.of(gathering1,gathering2,gathering3,gathering4,gathering5));
        for(int i=0;i<5;i++){
            recommendRepository.updateCount(gathering1.getId(), LocalDate.now(),1);
        }
        List<GatheringsQuery> gatheringDetailQueries = gatheringRepository.gatheringsRecommend(LocalDate.now());
        assertThat(gatheringDetailQueries).hasSize(1);
    }
    @Test
    void updateCount(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage);
        Enrollment enrollment1 = returnDummyEnrollment(user1,gathering);
        gathering.enroll(List.of(enrollment1));
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(List.of(user1));
        gatheringRepository.save(gathering);
        enrollmentRepository.saveAll(List.of(enrollment1));
        gatheringRepository.updateCount(gathering.getId(),10);
        em.flush();
        em.clear();
        Gathering fetchGathering = gatheringRepository.findById(gathering.getId())
                .orElseThrow(() -> new NotFoundGatheringException("Not Found Gathering"));
        assertThat(fetchGathering).extracting("count").isEqualTo(11);
    }



}
