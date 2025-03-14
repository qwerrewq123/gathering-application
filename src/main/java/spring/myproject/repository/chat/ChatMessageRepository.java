package spring.myproject.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.ChatMessage;
import spring.myproject.domain.ChatParticipant;
import spring.myproject.domain.ChatRoom;
import spring.myproject.dto.response.chat.ChatMessageResponse;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomAndChatParticipant(ChatRoom chatRoom, ChatParticipant chatParticipant);

    @Query("select new spring.myproject.domain.chat.dto.response." +
            "ChatMessageResponse(cm.chatRoom.id,cm.content,p.user.username,r.status) from ChatMessage cm " +
            "left join cm.chatParticipant p " +
            "left join ReadStatus r on r.chatMessage.id = cm.id and r.chatParticipant.id = :chatParticipantId " +
            "where cm.chatRoom.id = :roomId " +
            "order by case when r.status = true then 0 else 1 end")
    List<ChatMessageResponse> fetchMessages(Long roomId, Long chatParticipantId);
}
