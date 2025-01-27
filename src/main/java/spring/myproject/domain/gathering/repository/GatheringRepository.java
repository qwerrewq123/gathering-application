package spring.myproject.domain.gathering.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.gathering.Gathering;

import java.util.List;
import java.util.Optional;

public interface GatheringRepository extends JpaRepository<Gathering,Long> {

    @Query("select g from Gathering  g " +
            "left join fetch g.category ca " +
            "left join fetch g.createBy cr " +
            "left join fetch g.gatheringImage im " +
            "left join fetch g.gatheringCount co " +
            "where g.title like '%:title%'")
    Page<Gathering> findPaging(Pageable pageable, String title);

    @Query("select g from Like l  " +
            "join l.gathering g " +
            "left join fetch g.category ca " +
            "left join fetch g.createBy cr " +
            "left join fetch g.gatheringImage im " +
            "left join fetch g.gatheringCount co " +
            "where l.likedBy.id =: userId")
    Page<Gathering> findLikePaging(Pageable pageable, Long userId);

    @Query("select g from Recommend r " +
            "join r.gathering g " +
            "left join fetch g.category ca " +
            "left join fetch g.createBy cr " +
            "left join fetch g.gatheringImage im " +
            "left join fetch g.gatheringCount co")
    List<Gathering> findRecommendPaging();

    @Query("select g from Gathering g left join fetch g.gatheringCount gc where g.id = :gatheringId")
    Optional<Gathering> findGatheringAndCount(Long gatheringId);
}
