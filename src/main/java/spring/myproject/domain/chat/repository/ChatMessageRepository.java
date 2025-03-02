package spring.myproject.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.chat.ChatMessage;
import spring.myproject.domain.chat.ChatParticipant;
import spring.myproject.domain.chat.ChatRoom;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Optional<ChatMessage> findByChatRoomAndChatParticipant(ChatRoom chatRoom, ChatParticipant chatParticipant);
}
