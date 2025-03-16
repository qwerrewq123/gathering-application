package spring.myproject.repository.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.image.Image;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Long> {
    @Query("select i.url from Image i join i.gathering g")
    List<String> gatheringImage(Long gatheringId);
}
