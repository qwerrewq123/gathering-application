package spring.myproject.domain.recommend.service.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.myproject.domain.category.Category;
import spring.myproject.domain.category.repository.CategoryRepository;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.gathering.repository.GatheringRepository;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.recommend.Recommend;
import spring.myproject.domain.recommend.repository.RecommendRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.util.DummyData.*;

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
    void resetCount(){
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
        recommendRepository.save(recommend);

        recommendRepository.resetCount();
        Optional<Recommend> optionalRecommend = recommendRepository.findById(recommend.getId());
        em.flush();

        assertThat(optionalRecommend.get().getCount()).isEqualTo(0);
    }
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


        recommendRepository.updateCount(gathering.getId());
        Optional<Recommend> optionalRecommend = recommendRepository.findById(recommend.getId());
        assertThat(optionalRecommend).isNotNull();
        assertThat(optionalRecommend.get()).extracting("count").isEqualTo(2);
    }
    @Test
    void fetchTop5(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage);
        Recommend recommend1 = returnDummyRecommend(gathering,1);
        Recommend recommend2 = returnDummyRecommend(gathering,2);
        Recommend recommend3 = returnDummyRecommend(gathering,3);
        Recommend recommend4 = returnDummyRecommend(gathering,4);
        Recommend recommend5 = returnDummyRecommend(gathering,5);
        Recommend recommend6 = returnDummyRecommend(gathering,5);

        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(List.of(user1));
        gatheringRepository.saveAll(List.of(gathering));
        recommendRepository.saveAll(List.of(recommend1,recommend2,recommend3,recommend4,recommend5,recommend6));

        List<Recommend> recommends = recommendRepository.fetchTop5();
        assertThat(recommends.size()).isEqualTo(5);
    }
}
