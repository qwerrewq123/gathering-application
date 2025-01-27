package spring.myproject.domain.gathering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.gathering.GatheringCount;

public interface GatheringCountRepository extends JpaRepository<GatheringCount,Long> {
}
