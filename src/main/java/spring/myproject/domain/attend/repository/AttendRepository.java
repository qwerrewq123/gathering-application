package spring.myproject.domain.attend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.attend.Attend;

public interface AttendRepository extends JpaRepository<Attend,Long> {
    @Query("select a from Attend  a where a.attendBy.id = :id and a.meeting.id = : meetingId")
    Attend findByUserIdAndMeetingId(Long userId,Long meetingId);
}
