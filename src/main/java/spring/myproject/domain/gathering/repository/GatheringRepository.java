package spring.myproject.domain.gathering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.gathering.Gathering;

public interface GatheringRepository extends JpaRepository<Gathering,Long> {
}
