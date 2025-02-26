package spring.myproject.domain.gathering.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.myproject.domain.category.Category;
import spring.myproject.domain.category.repository.CategoryRepository;
import spring.myproject.domain.enrollment.repository.EnrollmentRepository;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.gathering.GatheringCount;
import spring.myproject.domain.gathering.dto.response.GatheringQueryDto;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static spring.myproject.util.DummyData.*;
import static spring.myproject.util.DummyData.returnDummyGathering;

@SpringBootTest
class GatheringCountRepositoryTest {
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
    EntityManager em;
    @Test
    void addCount(){
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        User user = returnDummyUser(1, userImage);
        GatheringCount gatheringCount = returnDummyGatheringCount();
        Gathering gathering = returnDummyGathering(1, category, user, gatheringImage, gatheringCount);
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.save(user);
        gatheringCountRepository.save(gatheringCount);
        gatheringRepository.saveAll(List.of(gathering));
        em.flush();

        gatheringCountRepository.addCount(gathering.getId());
        List<GatheringQueryDto> gatheringAndCount = gatheringRepository.findGatheringAndCount(gathering.getId());

        assertThat(gatheringAndCount.getFirst().getCount()).isEqualTo(2);

    }
  
}