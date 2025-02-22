package spring.myproject.domain.user.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.myproject.domain.user.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    void findByUsername(){
        User userA = User.builder()
                .username("user1")
                .email("user1@gmail.com")
                .build();

        userRepository.save(userA);
        userRepository.findByUsername("user1");
    }


}