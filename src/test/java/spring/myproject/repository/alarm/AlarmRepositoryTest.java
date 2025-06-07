package spring.myproject.repository.alarm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.entity.alarm.Alarm;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;


import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.utils.DummyData.*;

@SpringBootTest
@Transactional
class AlarmRepositoryTest {

    @Autowired
    AlarmRepository alarmRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;
    Image image;
    User user;
    List<Alarm> alarms;
    @BeforeEach
    void beforeEach(){
        image = returnDummyImage(1);
        user = returnDummyUser(1,image);
        alarms = List.of(returnDummyAlarm(1, user),
                returnDummyAlarm(2, user),
                returnDummyAlarm(3, user),
                returnDummyAlarm(4, user));

    }
    @Test
    void findUncheckedAlarmPage(){

        imageRepository.save(image);
        userRepository.save(user);
        alarmRepository.saveAll(alarms);

        Page<Alarm> page = alarmRepository.findUncheckedAlarmPage(PageRequest.of(0, 1), user.getId());

        assertThat(page.getTotalPages()).isEqualTo(4);
        assertThat(page.getTotalElements()).isEqualTo(4);

    }

    @Test
    void findCheckedAlarmPage(){

        Alarm alarm1 = alarms.get(0);
        Alarm alarm2 = alarms.get(1);
        alarm1.setChecked(true);
        alarm2.setChecked(true);
        imageRepository.save(image);
        userRepository.save(user);
        alarmRepository.saveAll(alarms);

        Page<Alarm> page = alarmRepository.findCheckedAlarmPage(PageRequest.of(0, 1), user.getId());

        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(2);
    }
}