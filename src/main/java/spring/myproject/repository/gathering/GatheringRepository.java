package spring.myproject.repository.gathering;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.myproject.dto.response.gathering.querydto.GatheringDetailQuery;
import spring.myproject.dto.response.gathering.querydto.GatheringsQuery;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.dto.response.gathering.querydto.MainGatheringsQuery;

import java.time.LocalDate;
import java.util.List;


public interface GatheringRepository extends JpaRepository<Gathering,Long> {
    @Query("select " +
            "new spring.myproject.dto.response.gathering.querydto.GatheringDetailQuery" +
            "(g.id,g.title,g.content,g.registerDate,ca.name,cr.username,crm.url,u.username,u.nickname,pm.url,im.url,g.count) " +
            "from Gathering g " +
            "left join g.createBy cr " +
            "left join cr.profileImage crm " +
            "left join g.category ca " +
            "left join g.gatheringImage im " +
            "left join g.enrollments e " +
            "left join e.enrolledBy u " +
            "left join u.profileImage pm " +
            "where g.id = :gatheringId")
    List<GatheringDetailQuery> gatheringDetail(Long gatheringId);

    @Query(value = "select id,title,content,registerDate,category,createdBy,url,count from ( " +
            "  select g.id as id, g.title as title, g.content as content, g.register_date as registerDate, ca.name as category, " +
            "         cr.username as createdBy, im.url as url, g.count as count, " +
            "         row_number() over (partition by ca.id order by g.count desc) as rownum " +
            "  from gathering g " +
            "  left join category ca on g.category_id = ca.id " +
            "  left join user cr on g.user_id = cr.id " +
            "  left join image im on g.image_id = im.id " +
            "  where g.title like concat('%', :title, '%') " +
            ") as subquery " +
            "where rownum between 1 and 9", nativeQuery = true)
    List<MainGatheringsQuery> gatherings(@Param("title") String title);

    @Query("select new spring.myproject.dto.response.gathering.querydto." +
            "GatheringsQuery(g.id,g.title,g.content,g.registerDate,ca.name,cr.username,im.url,g.count) " +
            "from Gathering  g " +
            "left join  g.category ca " +
            "left join  g.createBy cr " +
            "left join  g.gatheringImage im " +
            "where ca.name =:category"
    )
    Page<GatheringsQuery> gatheringsCategory(PageRequest pageRequest, String category);

    @Query("select new spring.myproject.dto.response.gathering.querydto." +
            "GatheringsQuery(g.id,g.title,g.content,g.registerDate,ca.name,cr.username,im.url,g.count) " +
            "from Gathering g left join g.gatheringImage im " +
            "left join g.category ca left join g.createBy cr " +
            "left join Like l on l.gathering.id = g.id left join l.likedBy u " +
            "where u.id=:userId")
    Page<GatheringsQuery> gatheringsLike(Pageable pageable, Long userId);

    @Query("select new spring.myproject.dto.response.gathering.querydto." +
            "GatheringsQuery(g.id,g.title,g.content,g.registerDate,ca.name,cr.username,im.url,g.count) " +
            "from Recommend r " +
            "join r.gathering g " +
            "join g.category ca " +
            "join g.createBy cr " +
            "left join g.gatheringImage im " +
            "where r.localDate = :localDate order by r.score desc limit 10")
    List<GatheringsQuery> gatheringsRecommend(LocalDate localDate);
}
