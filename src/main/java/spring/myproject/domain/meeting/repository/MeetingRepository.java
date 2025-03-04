package spring.myproject.domain.meeting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.meeting.Meeting;
import spring.myproject.domain.meeting.dto.response.MeetingsQuery;
import spring.myproject.domain.meeting.dto.response.MeetingDetailQuery;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {

    @Query("select new spring.myproject.domain.meeting.dto.response." +
            "MeetingDetailQuery(m.id,m.title,cr.username,u.username,m.boardDate,m.startDate,m.endDate,m.content,m.count,i.url) " +
            "from Meeting m left join m.attends a left join a.attendBy u join m.createdBy cr left join m.image i " +
            "where m.id = :meetingId and a.accepted = true ")
    List<MeetingDetailQuery> meetingDetail(Long meetingId);
    @Query("select new spring.myproject.domain.meeting.dto.response." +
            "MeetingsQuery(m.id,m.title,cr.username,m.boardDate,m.startDate,m.endDate,m.content,m.count,i.url) " +
            "from Meeting m left join m.createdBy cr left join m.image i " +
            "where m.title like %:title%")
    Page<MeetingsQuery> meetings(PageRequest pageRequest, String title);
}
