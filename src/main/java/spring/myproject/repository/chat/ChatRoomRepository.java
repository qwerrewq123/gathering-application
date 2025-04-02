package spring.myproject.repository.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.dto.response.chat.query.MyChatRoomElement;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.dto.response.chat.query.ChatRoomElement;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("select new spring.myproject.dto.response.chat.query." +
            "ChatRoomElement(c.id,c.name,c.count,u.username," +
            "(select case when sub_cp.status is true then true else false end " +
            "from ChatParticipant sub_cp left join sub_cp.user sub_u left join sub_cp.chatRoom sub_c where sub_u.id = :userId)) " +
            "from ChatRoom c " +
            "left join c.createdBy u " +
            "left join ChatParticipant cp on cp.chatRoom.id = c.id " +
            "left join cp.user cpu " +
            "order by case when cpu.id is not null then 0 else 1 end, c.id asc")
    Page<ChatRoomElement> fetchChatRooms(Pageable pageable, Long userId);

    @Query("select new spring.myproject.dto.response.chat.query." +
            "MyChatRoomElement(c.id,c.name,c.count,u.username,true," +
            "(select count(r.id) from ReadStatus r " +
            "left join r.chatParticipant cp left join cp.user cu " +
            "where cu.id=:userId and r.status=false)) " +
            "from ChatParticipant p " +
            "left join p.chatRoom c " +
            "left join c.createdBy u " +
            "where p.id = :userId " +
            "group by c.id")
    Page<MyChatRoomElement> fetchMyChatRooms(Pageable pageable, Long userId);

}
