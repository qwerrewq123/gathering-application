package spring.myproject.domain.chat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.chat.ChatRoom;
import spring.myproject.domain.chat.dto.response.ChatMyRoomResponse;
import spring.myproject.domain.chat.dto.response.ChatRoomResponse;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("select new spring.myproject.domain.chat.dto.response." +
            "ChatRoomResponse(c.name,c.count,u.username," +
            "case when cp.id is not null then true else false end) from ChatRoom c join  c.createdBy u " +
            "left join ChatParticipant cp on cp.chatRoom.id == c.id " +
            "where cp.user.id = :userId " +
            "order by case when cp.id is not null then 0 else 1 end, c.id asc")
    Page<ChatRoomResponse> fetchChatRooms(Pageable pageable,Long userId);

    @Query("select new spring.myproject.domain.chat.dto.response." +
            "ChatMyRoomResponse(c.name,c.count,u.username,true,count(r.id)) " +
            "from ChatParticipant p join p.chatRoom c join c.createdBy u " +
            "left join ReadStatus r on r.chatParticipant.id = p.id and r.status=false " +
            "where p.id = :userId " +
            "group by c.id")
    Page<ChatMyRoomResponse> fetchMyChatRooms(Pageable pageable, Long userId);
}
