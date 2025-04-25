package spring.myproject.repository.recommend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.recommend.Recommend;

import java.util.List;

public interface RecommendRepository extends JpaRepository<Recommend,Long> {

    @Query("update Recommend r set r.score = 0")
    @Modifying
    void resetCount();

    @Query(value =
            "update recommend r set r.score = r.score + :val " +
                    "where r.gathering_id = :gatheringId"
            ,nativeQuery = true)
    @Modifying
    int updateCount(Long gatheringId,int val);

    @Query("select r from Recommend r order by r.score desc limit 5")
    List<Recommend> fetchTop5();


}
