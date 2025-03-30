package spring.myproject.repository.like;

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
    LikeRepository likeRepository;

    @Test
    void findLike(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        User user1 = returnDummyUser(1, userImage);
        User user2 = returnDummyUser(2, userImage);
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage);
        Like like = returnDummyLike(user2, gathering);
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(List.of(user1,user2));
        gatheringRepository.saveAll(List.of(gathering));
        likeRepository.save(like);

        Optional<Like> optionalLike = likeRepository.findLike(user2.getId(), gathering.getId());

        assertThat(optionalLike).isPresent();
        assertThat(optionalLike.get()).extracting("likedBy").isEqualTo(user2);
    }
}