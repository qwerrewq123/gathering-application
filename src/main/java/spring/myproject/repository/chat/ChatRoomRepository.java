package spring.myproject.repository.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.dto.response.chat.ChatMyRoomResponse;
import spring.myproject.dto.response.chat.ChatRoomResponse;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("select new spring.myproject.dto.response.chat." +
            "ChatRoomResponse(c.name,c.count,u.username," +
            "case when cp.id is not null then true else false end) " +
            "from ChatRoom c left join c.createdBy u " +
            "left join ChatParticipant cp on cp.chatRoom.id = c.id " +
            "order by case when cp.id is not null then 0 else 1 end, c.id asc")
    Page<ChatRoomResponse> fetchChatRooms(Pageable pageable,Long userId);

    @Query("select new spring.myproject.dto.response.chat." +
            "ChatMyRoomResponse(c.name,c.count,u.username,true," +
            "(select count(r.id) from ReadStatus r where r.chatParticipant.id = p.id and r.status = false))" +
            "from ChatParticipant p " +
            "left join p.chatRoom c " +
            "left join c.createdBy u " +
            "where p.id = :userId " +
            "group by c.id")
    Page<ChatMyRoomResponse> fetchMyChatRooms(Pageable pageable, Long userId);

}
