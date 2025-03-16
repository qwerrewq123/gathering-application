package spring.myproject.repository.alarm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.fcm.Alarm;

public interface AlarmRepository extends JpaRepository<Alarm,Long> {

    @Query("select a from Alarm a join a.user u where u.id = :userId and a.checked = false")
    Page<Alarm> findUncheckedAlarmPage(Pageable pageable, Long userId);

    @Query("select a from Alarm a join a.user u where u.id = :userId and a.checked = true")
    Page<Alarm> findCheckedAlarmPage(Pageable pageable, Long userId);
}
