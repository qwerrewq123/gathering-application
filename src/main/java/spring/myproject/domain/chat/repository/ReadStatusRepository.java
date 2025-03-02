package spring.myproject.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.chat.ReadStatus;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {
    @Query("update ReadStatus r set r.status = true " +
            "where r.chatParticipant.id = :chatParticipantId and r.chatMessage.id =: chatMEssageId ")
    @Modifying
    void readChatMessage(Long chatParticipantId,Long chatMessageId);
}
