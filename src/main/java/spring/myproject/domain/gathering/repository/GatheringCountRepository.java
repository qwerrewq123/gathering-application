package spring.myproject.domain.gathering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.gathering.GatheringCount;

public interface GatheringCountRepository extends JpaRepository<GatheringCount,Long> {

    @Query("update GatheringCount g set g.count = g.count +1 where g.gathering.id =:gatheringId")
    @Modifying
    void addCount(Long gatheringId);
}
