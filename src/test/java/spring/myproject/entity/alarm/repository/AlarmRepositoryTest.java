package spring.myproject.entity.alarm.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import spring.myproject.entity.fcm.Alarm;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.repository.alarm.AlarmRepository;


import static org.assertj.core.api.Assertions.*;
import static spring.myproject.utils.DummyData.*;

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
        Alarm alarm2 = returnDummyAlarm(2, user);
        Alarm alarm3 = returnDummyAlarm(3, user);
        Alarm alarm4 = returnDummyAlarm(4, user);

        Page<Alarm> page = alarmRepository.findUncheckedAlarmPage(PageRequest.of(0, 1), user.getId());

        assertThat(page.getTotalPages()).isEqualTo(4);
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

        Page<Alarm> page = alarmRepository.findCheckedAlarmPage(PageRequest.of(0, 1), user.getId());

        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(2);
    }
}