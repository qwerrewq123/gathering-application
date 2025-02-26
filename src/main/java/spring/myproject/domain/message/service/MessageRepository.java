package spring.myproject.domain.message.service;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.message.Message;

public interface MessageRepository extends JpaRepository<Message,Long> {
}
