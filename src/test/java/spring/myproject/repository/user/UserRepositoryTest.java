package spring.myproject.repository.user;

import jakarta.persistence.EntityManager;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.utils.DummyData.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.yml")
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

        imageRepository.save(image);
        userRepository.saveAll(users);

        Boolean exist1 = userRepository.existsByUsername("user1");
        assertThat(exist1).isEqualTo(true);
    }
    @Test
    void existByNickname(){

        imageRepository.save(image);
        userRepository.saveAll(users);

        Boolean exist = userRepository.existsByNickname("nickname1");
        assertThat(exist).isEqualTo(true);
    }
    @Test
    void findByEmail(){


        imageRepository.save(image);
        userRepository.saveAll(users);

        List<User> userList1 = userRepository.findByEmail("email1");
        List<User> userList2 = userRepository.findByEmail("email2");

        assertThat(userList1.size()).isEqualTo(1);
        assertThat(userList1.getFirst()).extracting("email").isEqualTo("email1");
        assertThat(userList2.size()).isEqualTo(1);
        assertThat(userList2.getFirst()).extracting("email").isEqualTo("email2");

    }
    @Test
    void findById(){

        User user = users.get(0);
        imageRepository.save(image);
        userRepository.saveAll(users);

        Optional<User> optionalUser = userRepository.findById(user.getId());

        assertThat(optionalUser).isPresent();
        assertThat(optionalUser.get().getProfileImage() instanceof HibernateProxy).isFalse();
    }
}