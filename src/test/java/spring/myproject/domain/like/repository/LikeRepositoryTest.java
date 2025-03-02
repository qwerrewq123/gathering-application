package spring.myproject.domain.like.repository;

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
import spring.myproject.domain.like.Like;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static spring.myproject.util.DummyData.*;

@SpringBootTest
class LikeRepositoryTest {
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
    LikeRepository likeRepository;
    @Autowired
    EntityManager em;

    @Test
    void findLike(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        User user2 = returnDummyUser(2, userImage);
        GatheringCount gatheringCount = returnDummyGatheringCount();
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage, gatheringCount);
        Like like = returnLike(user2, gathering);
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(List.of(user1,user2));
        gatheringCountRepository.save(gatheringCount);
        gatheringRepository.saveAll(List.of(gathering));
        likeRepository.save(like);
        em.flush();

        Optional<Like> likeOptional = likeRepository.findLike(user2.getId(), gathering.getId());

        Assertions.assertThat(likeOptional.isPresent()).isTrue();

    }
}