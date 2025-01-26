package spring.myproject.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.like.Like;

public interface LikeRepository extends JpaRepository<Like,Long> {
}
