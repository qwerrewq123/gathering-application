package spring.myproject.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.chat.ChatMessage;
import spring.myproject.entity.chat.ChatParticipant;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.dto.response.chat.query.ChatMessageElement;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findChatMessageByChatRoom(ChatRoom chatRoom);

    @Query("select new spring.myproject.dto.response.chat.query." +
            "ChatMessageElement(cm.chatRoom.id,cm.content,p.user.username,r.status) from ChatMessage cm " +
            "left join cm.chatParticipant p " +
            "left join ReadStatus r on r.chatMessage.id = cm.id and r.chatParticipant.id = :chatParticipantId " +
            "left join ChatRoom cr on cr.id=cm.chatRoom.id " +
            "where cr.id= :roomId " +
            "order by case when r.status = true then 0 else 1 end")
    List<ChatMessageElement> fetchMessages(Long roomId, Long chatParticipantId);
}
