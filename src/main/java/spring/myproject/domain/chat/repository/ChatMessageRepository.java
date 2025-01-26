package spring.myproject.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.chat.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
}
