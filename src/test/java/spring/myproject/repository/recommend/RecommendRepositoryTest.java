package spring.myproject.repository.recommend;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.entity.category.Category;
import spring.myproject.repository.category.CategoryRepository;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.recommend.Recommend;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.utils.DummyData.*;
@Transactional
@SpringBootTest
public class RecommendRepositoryTest {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    GatheringRepository gatheringRepository;
    @Autowired
    RecommendRepository recommendRepository;
    @Autowired
    EntityManager em;


    @Test
    void updateCount(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage);
        Recommend recommend = returnDummyRecommend(gathering,1);

        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(List.of(user1));
        gatheringRepository.saveAll(List.of(gathering));
        recommendRepository.saveAll(List.of(recommend));


        recommendRepository.updateCount(gathering.getId(), LocalDate.now(),10);
        em.flush();
        em.clear();
        Recommend fetchRecommend = recommendRepository.findById(recommend.getId())
                        .orElseThrow(()-> new RuntimeException("Not Found Recommend"));
        assertThat(fetchRecommend).isNotNull();
        assertThat(fetchRecommend).extracting("score").isEqualTo(11L);
    }

}
