package spring.myproject.entity.user.repository;

import jakarta.persistence.EntityManager;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.utils.DummyData.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    EntityManager em;
    @Test
    void findByUsername(){
        Image image1 = returnDummyImage(1);
        User user1 = returnDummyUser(1,image1);
        User user2 = returnDummyUser(2,image1);

        imageRepository.save(image1);
        userRepository.save(user1);
        userRepository.save(user2);

        Optional<User> user = userRepository.findByUsername("user1");
        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo("user1");
    }
    @Test
    void existByUsername(){
        Image image1 = returnDummyImage(1);
        User user1 = returnDummyUser(1,image1);
        User user2 = returnDummyUser(2,image1);

        imageRepository.save(image1);
        userRepository.save(user1);
        userRepository.save(user2);

        Boolean exist1 = userRepository.existsByUsername("user1");
        assertThat(exist1).isEqualTo(true);
    }
    @Test
    void existByNickname(){
        Image image1 = returnDummyImage(1);
        User user1 = returnDummyUser(1,image1);
        User user2 = returnDummyUser(2,image1);

        imageRepository.save(image1);
        userRepository.save(user1);
        userRepository.save(user2);

        Boolean exist1 = userRepository.existsByNickname("nickname1");
        assertThat(exist1).isEqualTo(true);
    }
    @Test
    void findByEmail(){

        Image image = returnDummyImage(1);
        User user1 = returnDummyUser(1,image);
        User user2 = returnDummyUser(2,image);
        User user3 = returnDummyUser(3,image);
        User user4 = returnDummyUser(4,image);

        imageRepository.save(image);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

        List<User> userList1 = userRepository.findByEmail("email1");
        List<User> userList2 = userRepository.findByEmail("email2");
        List<User> userList3 = userRepository.findByEmail("email3");
        List<User> userList4 = userRepository.findByEmail("email4");

        assertThat(userList1.size()).isEqualTo(1)
                    .extracting("email")
                            .isEqualTo("email1");
        assertThat(userList2.size()).isEqualTo(1)
                    .extracting("email")
                        .isEqualTo("email2");
        assertThat(userList3.size()).isEqualTo(1)
                    .extracting("email")
                        .isEqualTo("email3");
        assertThat(userList4.size()).isEqualTo(1)
                    .extracting("email")
                        .isEqualTo("email4");
    }
    @Test
    void findById(){

        Image image = returnDummyImage(1);
        User user1 = returnDummyUser(1,image);
        User user2 = returnDummyUser(2,image);

        imageRepository.save(image);
        userRepository.save(user1);
        userRepository.save(user2);

        Optional<User> optionalUser1 = userRepository.findById(user1.getId());

        assertThat(optionalUser1).isPresent();
        assertThat(optionalUser1.get().getProfileImage() instanceof HibernateProxy).isFalse();
    }




}