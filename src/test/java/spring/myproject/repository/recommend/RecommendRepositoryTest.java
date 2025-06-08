package spring.myproject.repository.recommend;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
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
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.yml")
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
    Category category;
    Image userImage;
    Image gatheringImage;
    User user;
    Gathering gathering;
    Recommend recommend;

    @BeforeEach
    void beforeEach(){
        category = returnDummyCategory(1);
        userImage = returnDummyImage(1);
        gatheringImage = returnDummyImage(1);
        user = returnDummyUser(1, userImage);
        gathering = returnDummyGathering(1, category, user, gatheringImage);
        recommend = returnDummyRecommend(gathering,1);

    }

    @Test
    void updateCount(){

        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.save(user);
        gatheringRepository.save(gathering);
        recommendRepository.save(recommend);
        recommendRepository.updateCount(gathering.getId(), LocalDate.now(),10);
        em.flush();
        em.clear();

        Optional<Recommend> optionalRecommend = recommendRepository.findById(recommend.getId());

        assertThat(optionalRecommend).isPresent()
                .get().extracting("score").isEqualTo(11L);
    }

}
