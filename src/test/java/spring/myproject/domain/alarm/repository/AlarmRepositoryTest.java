package spring.myproject.domain.alarm.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import spring.myproject.domain.alarm.Alarm;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;


import static org.assertj.core.api.Assertions.*;
import static spring.myproject.util.DummyData.*;

@SpringBootTest
class AlarmRepositoryTest {

    @Autowired
    AlarmRepository alarmRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;
    @Test
    void findUncheckedAlarmPage(){
        Image image = returnDummyImage(1);
        User user = returnDummyUser(1, image);
        Alarm alarm1 = returnDummyAlarm(1, user);
        Alarm alarm2 = returnDummyAlarm(1, user);
        Alarm alarm3 = returnDummyAlarm(1, user);
        Alarm alarm4 = returnDummyAlarm(1, user);

        Page<Alarm> page = alarmRepository.findUncheckedAlarmPage(PageRequest.of(0, 10), user.getId());

        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(4);

    }

    @Test
    void findCheckedAlarmPage(){
        Image image = returnDummyImage(1);
        User user = returnDummyUser(1, image);
        Alarm alarm1 = returnDummyAlarm(1, user);
        Alarm alarm2 = returnDummyAlarm(1, user);
        Alarm alarm3 = returnDummyAlarm(1, user);
        Alarm alarm4 = returnDummyAlarm(1, user);
        alarm1.setChecked(true);
        alarm2.setChecked(true);

        Page<Alarm> page = alarmRepository.findCheckedAlarmPage(PageRequest.of(0, 10), user.getId());

        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
    }
}