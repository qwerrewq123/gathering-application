package spring.myproject.repository.user;

import jakarta.persistence.EntityManager;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.utils.DummyData.*;
@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    EntityManager em;
    List<User> users;
    Image image;
    @BeforeEach
    void beforeEach(){
        image = returnDummyImage(1);
        users = List.of(returnDummyUser(1,image),
                returnDummyUser(2,image)
                );
    }


    @Test
    void existByUsername(){
        User user1 = users.get(0);
        User user2 = users.get(1);

        imageRepository.save(image);
        userRepository.save(user1);
        userRepository.save(user2);

        Boolean exist1 = userRepository.existsByUsername("user1");
        assertThat(exist1).isEqualTo(true);
    }
    @Test
    void existByNickname(){
        User user1 = users.get(0);
        User user2 = users.get(1);

        imageRepository.save(image);
        userRepository.save(user1);
        userRepository.save(user2);

        Boolean exist = userRepository.existsByNickname("nickname1");
        assertThat(exist).isEqualTo(true);
    }
    @Test
    void findByEmail(){

        User user1 = users.get(0);
        User user2 = users.get(1);

        imageRepository.save(image);
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> userList1 = userRepository.findByEmail("email1");
        List<User> userList2 = userRepository.findByEmail("email2");

        assertThat(userList1.size()).isEqualTo(1);
        assertThat(userList1.getFirst()).extracting("email").isEqualTo("email1");
        assertThat(userList2.size()).isEqualTo(1);
        assertThat(userList2.getFirst()).extracting("email").isEqualTo("email2");

    }
    @Test
    void findById(){

        User user1 = users.get(0);
        User user2 = users.get(1);

        imageRepository.save(image);
        userRepository.save(user1);
        userRepository.save(user2);

        Optional<User> optionalUser1 = userRepository.findById(user1.getId());

        assertThat(optionalUser1).isPresent();
        assertThat(optionalUser1.get().getProfileImage() instanceof HibernateProxy).isFalse();
    }
}