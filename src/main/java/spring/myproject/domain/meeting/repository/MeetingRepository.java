package spring.myproject.domain.meeting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.meeting.Meeting;
import spring.myproject.domain.user.User;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {

    @Query("select u from Meeting m join m.attends a join a.attendBy u where m.id = :meetingId")
    List<User> findAttendsBy(Long meetingId);
    @Query("select m from Meeting m where m.title like :title")
    Page<Meeting> meetings(PageRequest pageRequest, String title);
}
