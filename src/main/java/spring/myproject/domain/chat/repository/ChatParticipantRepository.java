package spring.myproject.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.chat.ChatParticipant;
import spring.myproject.domain.chat.ChatRoom;
import spring.myproject.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    Optional<ChatParticipant> findByChatRoomAndUserAndStatus(ChatRoom chatRoom, User user,boolean status);
    List<ChatParticipant> findAllByChatRoomAndStatus(ChatRoom chatRoom, boolean status);

}
