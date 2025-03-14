package spring.myproject.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.ChatParticipant;
import spring.myproject.domain.ChatRoom;
import spring.myproject.domain.User;

import java.util.List;
import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    Optional<ChatParticipant> findByChatRoomAndUserAndStatus(ChatRoom chatRoom, User user,boolean status);
    List<ChatParticipant> findAllByChatRoomAndStatus(ChatRoom chatRoom, boolean status);

}
