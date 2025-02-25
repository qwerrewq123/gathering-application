package spring.myproject.domain.gathering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.gathering.GatheringCount;

public interface GatheringCountRepository extends JpaRepository<GatheringCount,Long> {

    @Query("update GatheringCount gc set gc.count = gc.count+1 " +
            "where gc.id = (select g.id from Gathering g where g.gatheringCount.id = gc.id)")
    @Modifying
    void addCount(Long gatheringId);

}
