package spring.myproject.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.category.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByName(String name);
}
