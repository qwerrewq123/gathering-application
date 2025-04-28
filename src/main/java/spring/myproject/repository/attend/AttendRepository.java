package spring.myproject.repository.attend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.attend.Attend;

import java.util.Optional;

public interface AttendRepository extends JpaRepository<Attend,Long> {
    @Query("select a from Attend  a where a.attendBy.id = :userId and a.meeting.id = :meetingId")
    Attend findByUserIdAndMeetingId(Long userId,Long meetingId);

}
