package spring.myproject.domain.gathering;

import jakarta.persistence.*;
import lombok.*;
import spring.myproject.domain.category.Category;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@NoArgsConstructor
@Setter
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createBy;

    @OneToMany(mappedBy = "gathering")
    private List<User> participatedBy = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image gatheringImage;

    @OneToOne(mappedBy = "gathering",fetch = FetchType.LAZY)
    private GatheringCount gatheringCount;

}
