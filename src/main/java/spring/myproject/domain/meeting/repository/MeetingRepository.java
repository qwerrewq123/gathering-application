package spring.myproject.domain.meeting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.meeting.Meeting;
import spring.myproject.domain.user.User;
import spring.myproject.dto.response.meeting.MeetingListResponse;
import spring.myproject.dto.response.meeting.MeetingQueryListResponse;
import spring.myproject.dto.response.meeting.MeetingQueryResponse;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {

    @Query("select new spring.myproject.dto.response.meeting." +
            "MeetingQueryResponse(m.id,m.title,cr.username,u.username,m.boardDate,m.startDate,m.endDate,m.content) " +
            "from Meeting m left join m.attends a left join a.attendBy u join m.createdBy cr " +
            "where m.id = :meetingId")
    List<MeetingQueryResponse> findAttendsBy(Long meetingId);
    @Query("select new spring.myproject.dto.response.meeting." +
            "MeetingQueryListResponse(m.id,m.title,cr.username,m.boardDate,m.startDate,m.endDate,m.content) " +
            "from Meeting m join m.createdBy cr " +
            "where m.title like %:title%")
    Page<MeetingQueryListResponse> meetings(PageRequest pageRequest, String title);
}
