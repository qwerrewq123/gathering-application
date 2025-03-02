package spring.myproject.domain.chat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.chat.ChatRoom;
import spring.myproject.domain.chat.dto.response.ChatRoomResponse;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("select new spring.myproject.domain.chat.dto.response." +
            "ChatRoomResponse(c.name,c.count,u.username) from ChatRoom c join fetch c.createdBy u")
    Page<ChatRoomResponse> fetchChatRooms(Pageable pageable);

    @Query("select new spring.myproject.domain.chat.dto.response." +
            "ChatRoomResponse(c.name,c.count,u.username) " +
            "from ChatParticipant p join p.chatRoom c join c.createdBy u where p.id = :userId")
    Page<ChatRoomResponse> fetchMyChatRooms(Pageable pageable,Long userId);
}
