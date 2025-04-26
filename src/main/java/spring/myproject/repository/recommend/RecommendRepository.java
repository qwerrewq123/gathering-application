package spring.myproject.repository.recommend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.recommend.Recommend;

import java.time.LocalDate;
import java.util.List;

public interface RecommendRepository extends JpaRepository<Recommend,Long> {

    @Query(value =
            "insert into recommend(gathering_id,score,date) " +
                    "values (:gatheringId,1,:localDate) " +
                    "on duplicate key update score = score + :val"
            ,nativeQuery = true)
    @Modifying
    int updateCount(Long gatheringId,LocalDate localDate,int val);

}
