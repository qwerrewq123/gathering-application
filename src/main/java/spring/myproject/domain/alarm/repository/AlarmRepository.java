package spring.myproject.domain.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.alarm.Alarm;

public interface AlarmRepository extends JpaRepository<Alarm,Long> {
}
