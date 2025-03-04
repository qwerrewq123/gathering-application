package spring.myproject.domain.gathering.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    @Query(value = "select * from ( " +
            "  select g.id, g.title, g.content, g.register_date as registerDate, ca.name as categoryName, " +
            "         cr.username as createdBy, im.url as imageUrl, g.count, " +
            "         row_number() over (partition by ca.id order by g.id asc) as rownum " +
            "  from gathering g " +
            "  left join category ca on g.category_id = ca.id " +
            "  left join user cr on g.create_by = cr.id " +
            "  left join gathering_image im on g.id = im.gathering_id " +
            "  where g.title like concat('%', :title, '%') " +
            ") as subquery " +
            "where rownum between 1 and 9", nativeQuery = true)
    List<GatheringsQuery> gatherings(@Param("title") String title);

    @Query("select new spring.myproject.domain.gathering.dto.response." +
            "GatheringsQuery(g.id,g.title,g.content,g.registerDate,ca.name,cr.username,im.url,g.count) " +
            "from Gathering  g " +
            "left join  g.category ca " +
            "left join  g.createBy cr " +
            "left join  g.gatheringImage im " +
            "where ca.name =:category"
    )
    Page<GatheringsQuery> gatheringsCategory(PageRequest pageRequest, String category);

    @Query("select new spring.myproject.domain.gathering.dto.response." +
            "GatheringsQuery(g.id,g.title,g.content,g.registerDate,ca.name,cr.username,im.url,g.count) " +
            "from Gathering g left join g.gatheringImage im " +
            "left join g.category ca left join g.createBy cr " +
            "left join Like l on l.gathering.id = g.id left join l.likedBy u " +
            "where u.id=:userId")
    Page<GatheringsQuery> gatheringsLike(Pageable pageable, Long userId);

    @Query("select new spring.myproject.domain.gathering.dto.response." +
            "GatheringsQuery(g.id,g.title,g.content,g.registerDate,ca.name,cr.username,im.url,g.count) " +
            "from Recommend r " +
            "join r.gathering g " +
            "join g.category ca " +
            "join g.createBy cr " +
            "left join g.gatheringImage im")
    List<GatheringDetailQuery> gatherinsRecommend();
}
