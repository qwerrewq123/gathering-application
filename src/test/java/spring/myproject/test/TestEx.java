package spring.myproject.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class TestEx {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageRepository imageRepository;
    @PersistenceContext
    EntityManager em;
    @Autowired
    TransactionTemplate transactionTemplate;

    @Test
    void test() throws InterruptedException {

        Image image = imageRepository.findById(1L).get();
        User user = userRepository.findById(1L).get();


        int threadCount = 10; // 생성할 쓰레드 개수
        CountDownLatch latch = new CountDownLatch(6000); // 쓰레드 개수만큼 CountDownLatch 생성

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 6000; i++) {
            int finalI = i;
            executorService.submit(() -> {
                insert(user.getPassword(), finalI +1);
                latch.countDown();

            });
            latch.await();
            executorService.shutdown();

        }
    }

    void insert(String password,int i){
        transactionTemplate.executeWithoutResult((status)->{
            for (int j = 0; j < 2000*i; j++) {
                User build = User.builder()
                        .username("user" + j)
                        .age(j)
                        .hobby("hobby" + j)
                        .address("address" + j)
                        .email("email" + j)
                        .password(password)
                        .profileImage(null)
                        .build();

                em.persist(build);
            }


        });

    }

}
