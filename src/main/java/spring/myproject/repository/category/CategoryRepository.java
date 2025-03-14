package spring.myproject.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByName(String name);
}
