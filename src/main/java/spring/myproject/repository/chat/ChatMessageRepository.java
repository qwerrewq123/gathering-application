package spring.myproject.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.chat.ChatMessage;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.dto.response.chat.query.ChatMessageElement;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findChatMessageByChatRoom(ChatRoom chatRoom);

    @Query("select new spring.myproject.dto.response.chat.query." +
            "ChatMessageElement(cr.id,cm.id,cm.content,uu.username,r.status) " +
            "from ReadStatus r " +
            "left join r.chatParticipant cp left join cp.user u " +
            "left join r.chatMessage cm " +
            "left join cm.chatRoom cr left join cm.chatParticipant cpp left join cpp.user uu " +
            "where u.id =:userId and cr.id =:chatId and r.status = false"
    )
    List<ChatMessageElement> fetchUnReadMessages(Long chatId, Long userId);
}
