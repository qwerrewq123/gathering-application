package spring.myproject.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.category.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("select c from Category c left join c.gathering g where g.id =:gatheringId and c.name = :name")
    Optional<Category> findBy(Long gatheringId,String name);
}
