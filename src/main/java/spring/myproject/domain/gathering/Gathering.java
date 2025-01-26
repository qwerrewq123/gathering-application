package spring.myproject.domain.gathering;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.domain.category.Category;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@NoArgsConstructor
@Entity
@Table(name = "gathering")
@AllArgsConstructor
@Builder
public class Gathering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private LocalDateTime registerDate;
    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createBy;
    @OneToMany
    private List<User> participatedBy = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "image_id")
    private Image gatheringImage;

}
