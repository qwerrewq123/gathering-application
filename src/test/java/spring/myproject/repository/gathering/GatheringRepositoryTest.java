package spring.myproject.repository.gathering;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
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

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.yml")
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
        User user1 = users.get(0);
        User user2 = users.get(1);
        User user3 = users.get(2);
        gatherings = List.of(returnDummyGathering(1, category, user1, gatheringImage),
                returnDummyGathering(2, category, user1, gatheringImage),
                returnDummyGathering(3, category, user1, gatheringImage),
                returnDummyGathering(4, category, user1, gatheringImage),
                returnDummyGathering(5, category, user1, gatheringImage));
        Gathering gathering1 = gatherings.get(0);
        Gathering gathering2 = gatherings.get(1);
        Gathering gathering3 = gatherings.get(2);
        Gathering gathering4 = gatherings.get(3);
        Gathering gathering5 = gatherings.get(4);
        enrollments = List.of(returnDummyEnrollment(user1,gathering1),
                returnDummyEnrollment(user2,gathering1),
                returnDummyEnrollment(user3,gathering1),
                returnDummyEnrollment(user1,gathering2),
                returnDummyEnrollment(user1,gathering3),
                returnDummyEnrollment(user1,gathering4),
                returnDummyEnrollment(user1,gathering5));
        likes = List.of(returnDummyLike(user2, gathering1),
                returnDummyLike(user3, gathering1));

    }
    @Test
    void gatheringDetail(){

        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(users);
        gatheringRepository.saveAll(gatherings);
        enrollmentRepository.saveAll(enrollments);
        List<GatheringDetailQuery> gatheringDetailQueries = gatheringRepository.gatheringDetail(gatherings.get(0).getId());

        assertThat(gatheringDetailQueries).hasSize(3);
        assertThat(gatheringDetailQueries).extracting("participatedBy")
                .containsExactly(
                        "user1","user2","user3"
                );
    }

    @Test
    void gatheringsCategory(){

        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(users);
        gatheringRepository.saveAll(gatherings);

        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<GatheringsQuery> page = gatheringRepository.gatheringsCategory(pageRequest, category.getName());

        assertThat(page.getTotalPages()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
    }
    @Test
    void gatheringsLike(){

        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(users);
        gatheringRepository.saveAll(gatherings);
        likeRepository.saveAll(likes);

        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<GatheringsQuery> page1 = gatheringRepository.gatheringsLike(pageRequest, users.get(1).getId());
        Page<GatheringsQuery> page2 = gatheringRepository.gatheringsLike(pageRequest, users.get(2).getId());

        assertThat(page1.getTotalPages()).isEqualTo(1);
        assertThat(page1.getTotalElements()).isEqualTo(1);
        assertThat(page2.getTotalPages()).isEqualTo(1);
        assertThat(page2.getTotalElements()).isEqualTo(1);
    }
    @Test
    void gatheringsRecommend(){
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(users);
        gatheringRepository.saveAll(gatherings);
        em.flush();
        em.clear();
        for(int i=0;i<5;i++){
            recommendRepository.updateCount(gatherings.get(0).getId(), LocalDate.now(),1);
        }
        List<GatheringsQuery> gatheringDetailQueries = gatheringRepository.gatheringsRecommend(LocalDate.now());

        assertThat(gatheringDetailQueries).hasSize(1);
    }
    @Test
    void updateCount(){
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(users);
        gatheringRepository.saveAll(gatherings);
        enrollmentRepository.saveAll(enrollments);
        em.flush();
        em.clear();
        gatheringRepository.updateCount(gatherings.get(0).getId(),10);
        Gathering fetchGathering = gatheringRepository.findById(gatherings.get(0).getId())
                .orElseThrow(() -> new NotFoundGatheringException("Not Found Gathering"));

        assertThat(fetchGathering).extracting("count").isEqualTo(11);
    }



}
