package spring.myproject.domain.gathering;

import jakarta.persistence.*;
import lombok.*;
import spring.myproject.domain.category.Category;
import spring.myproject.domain.enrollment.Enrollment;
import spring.myproject.domain.gathering.dto.request.AddGatheringRequest;
import spring.myproject.domain.gathering.dto.request.UpdateGatheringRequest;
import spring.myproject.domain.gathering.dto.response.GatheringElement;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createBy;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image gatheringImage;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "gathering_id")
    private GatheringCount gatheringCount;

    @OneToMany(mappedBy = "gathering")
    List<Enrollment> enrollments = new ArrayList<>();

    public void changeGathering(Image image, Category category, UpdateGatheringRequest updateGatheringRequest){
        this.gatheringImage = image;
        this.category = category;
        this.title = updateGatheringRequest.getTitle();
        this.content = updateGatheringRequest.getContent();
        this.registerDate = LocalDateTime.now();
    }

    public static Gathering of(AddGatheringRequest addGatheringRequest, User createBy, Category category, Image image){
        return Gathering.builder()
                .title(addGatheringRequest.getTitle())
                .content(addGatheringRequest.getContent())
                .createBy(createBy)
                .category(category)
                .registerDate(LocalDateTime.now())
                .gatheringImage(image)
                .build();
    }
    public void enroll(List<Enrollment> enrollments){
        for (Enrollment enrollment : enrollments) {
            enrollment.enrollGathering(this);
        }
        this.enrollments = enrollments;
    }
}
