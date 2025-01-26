package spring.myproject.domain.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.image.Image;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
