package spring.myproject.domain.recommend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.recommend.Recommend;

public interface RecommendRepository extends JpaRepository<Recommend,Long> {
}
