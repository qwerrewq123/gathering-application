package spring.myproject.repository.fcm;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
