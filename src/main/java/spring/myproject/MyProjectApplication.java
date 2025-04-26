package spring.myproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyProjectApplication {
    //todo : 모임기반 알람 3시간전 알람
    public static void main(String[] args) {
        SpringApplication.run(MyProjectApplication.class, args);
    }

}
