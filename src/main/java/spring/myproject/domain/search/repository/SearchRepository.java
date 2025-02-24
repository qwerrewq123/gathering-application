package spring.myproject.domain.search.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.search.Search;

public interface SearchRepository extends JpaRepository<Search,Long> {
}
