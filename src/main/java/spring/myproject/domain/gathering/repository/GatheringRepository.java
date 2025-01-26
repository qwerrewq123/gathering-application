package spring.myproject.domain.gathering.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.gathering.Gathering;

import java.util.List;

public interface GatheringRepository extends JpaRepository<Gathering,Long> {

    @Query("select g from Gathering  g where g.title = :title")
    Page<Gathering> findPaging(Pageable pageable, String title);

    @Query("select l from Like l join Gathering g where l.likedBy.id =: userId")
    Page<Gathering> findLikePaging(Pageable pageable, Long userId);

    @Query("select g from Gathering g join Recommend r")
    List<Gathering> findRecommendPaging();
}
