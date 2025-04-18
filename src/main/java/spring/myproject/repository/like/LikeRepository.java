package spring.myproject.repository.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.like.Like;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {

    @Query("select l from Like l " +
            "where l.gathering.id = :gatheringId and l.likedBy.id = :userId")
    Optional<Like> findLike(Long userId, Long gatheringId);



}
