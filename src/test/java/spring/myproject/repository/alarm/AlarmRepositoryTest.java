package spring.myproject.repository.alarm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.entity.alarm.Alarm;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;


import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.utils.DummyData.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.yml")
class AlarmRepositoryTest {

    @Autowired
    AlarmRepository alarmRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;
    Image image;
    User user;
    List<Alarm> unCheckedAlarms;
    List<Alarm> checkedAlarms;
    @BeforeEach
    void beforeEach(){
        image = returnDummyImage(1);
        user = returnDummyUser(1,image);
        unCheckedAlarms = List.of(returnDummyAlarm(1, user,false),
                returnDummyAlarm(2, user,false),
                returnDummyAlarm(3, user,false),
                returnDummyAlarm(4, user,false));
        checkedAlarms = List.of(returnDummyAlarm(1, user,true),
                returnDummyAlarm(2, user,true),
                returnDummyAlarm(3, user,true),
                returnDummyAlarm(4, user,true));

    }
    @Test
    void findUncheckedAlarmPage(){

        imageRepository.save(image);
        userRepository.save(user);
        alarmRepository.saveAll(unCheckedAlarms);

        Page<Alarm> page = alarmRepository.findUncheckedAlarmPage(PageRequest.of(0, 1), user.getId());

        assertThat(page.getTotalPages()).isEqualTo(4);
        assertThat(page.getTotalElements()).isEqualTo(4);

    }

    @Test
    void findCheckedAlarmPage(){

        imageRepository.save(image);
        userRepository.save(user);
        alarmRepository.saveAll(checkedAlarms);

        Page<Alarm> page = alarmRepository.findCheckedAlarmPage(PageRequest.of(0, 1), user.getId());

        assertThat(page.getTotalPages()).isEqualTo(4);
        assertThat(page.getTotalElements()).isEqualTo(4);
    }
}