package spring.myproject.repository.recommend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.Recommend;

import java.util.List;

public interface RecommendRepository extends JpaRepository<Recommend,Long> {

    @Query("update Recommend r set r.count = 0")
    @Modifying
    void resetCount();
    @Query("update Recommend  r set r.count = r.count+1 where r.gathering.id = :gatehringId")
    @Modifying
    int updateCount(Long gatheringId);
    @Query("select r from Recommend r order by r.count desc limit 5")
    List<Recommend> fetchTop5();
}
