package spring.myproject.domain.fcm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.fcm.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
