package spring.myproject.domain.alarm.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import spring.myproject.domain.alarm.Alarm;
import spring.myproject.domain.user.repository.UserRepository;

import java.time.LocalDateTime;

@SpringBootTest
class AlarmRepositoryTest {

    @Autowired
    AlarmRepository alarmRepository;
    @Test
    void findUncheckedAlarmPage(){
    }

    @Test
    void findCheckedAlarmPage(){

    }
}