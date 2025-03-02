package spring.myproject.domain.gathering.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.gathering.dto.response.GatheringsQuery;
import spring.myproject.domain.gathering.dto.response.GatheringDetailQuery;

import java.util.List;

public interface GatheringRepository extends JpaRepository<Gathering,Long> {
    @Query("select " +
            "new spring.myproject.domain.gathering.dto.response." +
            "GatheringDetailQuery(g.id,g.title,g.content,g.registerDate,ca.name,cr.username,u.username,im.url,g.count) " +
            "from Gathering g " +
            "left join g.enrollments e " +
            "left join e.enrolledBy u " +
            "left join g.createBy cr " +
            "left join g.category ca " +
            "left join g.gatheringImage im " +
            "where g.id = :gatheringId")
    List<GatheringDetailQuery> gatheringDetail(Long gatheringId);


    @Query("select new spring.myproject.domain.gathering.dto.response." +
            "GatheringsQuery(g.id,g.title,g.content,g.registerDate,ca.name,cr.username,im.url,g.count) " +
            "from Gathering  g " +
            "left join  g.category ca " +
            "left join  g.createBy cr " +
            "left join  g.gatheringImage im " +
            "where g.title like concat('%', :title, '%')")
    Page<GatheringsQuery> gatherings(Pageable pageable, String title);

    @Query("select new spring.myproject.domain.gathering.dto.response." +
            "GatheringsQuery(g.id,g.title,g.content,g.registerDate,ca.name,cr.username,im.url,g.count) " +
            "from Like l  " +
            "join l.gathering g " +
            "left join g.category ca " +
            "left join g.createBy cr " +
            "left join g.gatheringImage im " +
            "where l.likedBy.id = :userId")
    Page<GatheringsQuery> gatheringsLike(Pageable pageable, Long userId);

    @Query("select new spring.myproject.domain.gathering.dto.response." +
            "GatheringsQuery(g.id,g.title,g.content,g.registerDate,ca.name,cr.username,im.url,g.count) " +
            "from Recommend r " +
            "join r.gathering g " +
            "join g.category ca " +
            "join g.createBy cr " +
            "left join g.gatheringImage im")
    List<GatheringDetailQuery> gatherinsRecommend();

    @Query("select new spring.myproject.domain.gathering.dto.response." +
            "GatheringsQuery(g.id,g.title,g.content,g.registerDate,ca.name,cr.username,im.url,g.count) " +
            "from Gathering  g " +
            "left join  g.category ca " +
            "left join  g.createBy cr " +
            "left join  g.gatheringImage im " +
            "where ca.name =:category"
    )
    Page<GatheringsQuery> gatheringsCategory(PageRequest pageRequest, String category);
}
