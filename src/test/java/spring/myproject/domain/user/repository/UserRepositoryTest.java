package spring.myproject.domain.user.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.user.User;
import spring.myproject.util.DummyData;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static spring.myproject.util.DummyData.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    EntityManager em;
    @Test
    void existByUsername(){
        Image image1 = returnDummyImage(1);
        User user1 = returnDummyUser(1,image1);
        User user2 = returnDummyUser(2,image1);

        imageRepository.save(image1);
        userRepository.save(user1);
        userRepository.save(user2);
        em.flush();

        Boolean exist1 = userRepository.existsByUsername("user1");
        Boolean exist2 = userRepository.existsByUsername("user2");
        assertThat(exist1).isEqualTo(true);
        assertThat(exist2).isEqualTo(true);
    }
    @Test
    void findByEmail(){

        Image image1 = returnDummyImage(1);
        Image image2 = returnDummyImage(2);
        User user1 = returnDummyUser(1,image1);
        User user2 = returnDummyUser(2,image1);
        User user3 = returnDummyUser(3,image2);
        User user4 = returnDummyUser(4,image2);

        imageRepository.save(image1);
        imageRepository.save(image2);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        em.flush();
        List<User> userList1 = userRepository.findByEmail("email1");
        List<User> userList2 = userRepository.findByEmail("email2");
        List<User> userList3 = userRepository.findByEmail("email3");
        List<User> userList4 = userRepository.findByEmail("email4");

        assertThat(userList1.size()).isEqualTo(1);
        assertThat(userList2.size()).isEqualTo(1);
        assertThat(userList3.size()).isEqualTo(1);
        assertThat(userList4.size()).isEqualTo(1);
    }




}