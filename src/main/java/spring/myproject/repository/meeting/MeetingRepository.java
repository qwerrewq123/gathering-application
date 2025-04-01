package spring.myproject.repository.meeting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.meeting.Meeting;
import spring.myproject.dto.response.meeting.querydto.MeetingsQuery;
import spring.myproject.dto.response.meeting.querydto.MeetingDetailQuery;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {

    @Query("select new spring.myproject.dto.response.meeting.querydto." +
            "MeetingDetailQuery(m.id,m.title,cr.username,u.username,m.boardDate,m.startDate,m.endDate,m.content,m.count,i.url) " +
            "from Meeting m " +
            "left join m.attends a " +
            "left join a.attendBy u " +
            "left join m.createdBy cr " +
            "left join m.image i on i.id=m.image.id " +
            "where m.id = :meetingId and a.accepted = true ")
    List<MeetingDetailQuery> meetingDetail(Long meetingId);
    @Query("select new spring.myproject.dto.response.meeting.querydto." +
            "MeetingsQuery(m.id,m.title,cr.username,m.boardDate,m.startDate,m.endDate,m.content,m.count,i.url) " +
            "from Meeting m left join m.createdBy cr left join m.image i " +
            "where m.title like %:title%")
    Page<MeetingsQuery> meetings(PageRequest pageRequest, String title);
}
