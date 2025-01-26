package spring.myproject.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.chat.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
}
