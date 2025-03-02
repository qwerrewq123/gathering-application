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
    GatheringCountRepository gatheringCountRepository;
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
        User user2 = returnDummyUser(2, userImage);
        GatheringCount gatheringCount = returnDummyGatheringCount();
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage, gatheringCount);
        Recommend recommend = returnRecommend(gathering);
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(List.of(user1,user2));
        gatheringCountRepository.save(gatheringCount);
        gatheringRepository.saveAll(List.of(gathering));
        recommendRepository.save(recommend);
        em.flush();

        recommendRepository.resetCount();
        Recommend fetchRecommend = recommendRepository.findById(recommend.getId()).orElse(null);

        Assertions.assertThat(fetchRecommend).isNotNull();
        Assertions.assertThat(fetchRecommend.getCount()).isEqualTo(0);

    }
    @Test
    void updateCount(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        User user2 = returnDummyUser(2, userImage);
        GatheringCount gatheringCount = returnDummyGatheringCount();
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage, gatheringCount);
        Recommend recommend = returnRecommend(gathering);
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(List.of(user1,user2));
        gatheringCountRepository.save(gatheringCount);
        gatheringRepository.saveAll(List.of(gathering));
        recommendRepository.save(recommend);
        em.flush();

        recommendRepository.updateCount(gathering.getId());
        Recommend fetchRecommend = recommendRepository.findById(recommend.getId()).orElse(null);

        Assertions.assertThat(fetchRecommend.getCount()).isEqualTo(2);

    }
    @Test
    void fetchTop5(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        User user2 = returnDummyUser(2, userImage);
        GatheringCount gatheringCount = returnDummyGatheringCount();
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage, gatheringCount);
        Recommend recommend1 = returnRecommend(gathering);
        Recommend recommend2 = returnRecommend(gathering);
        Recommend recommend3 = returnRecommend(gathering);
        Recommend recommend4 = returnRecommend(gathering);
        Recommend recommend5 = returnRecommend(gathering);
        recommend1.setCount(10L);
        recommend1.setCount(9L);
        recommend1.setCount(8L);
        recommend1.setCount(7L);
        recommend1.setCount(6L);
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(List.of(user1,user2));
        gatheringCountRepository.save(gatheringCount);
        gatheringRepository.saveAll(List.of(gathering));
        recommendRepository.saveAll(List.of(recommend1,recommend2,recommend3,recommend4,recommend5));
        em.flush();

        List<Recommend> recommendList = recommendRepository.fetchTop5();

        Assertions.assertThat(recommendList.getFirst().getCount()).isEqualTo(10L);
    }
}
