package spring.myproject.entity.gathering;

import jakarta.persistence.*;
import lombok.*;
import spring.myproject.entity.enrollment.Enrollment;
import spring.myproject.entity.fcm.Topic;
import spring.myproject.entity.image.Image;
import spring.myproject.entity.user.User;
import spring.myproject.entity.category.Category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static spring.myproject.dto.request.gathering.GatheringRequestDto.*;

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
    @Lob
    private String content;
    private LocalDateTime registerDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createBy;

    private int count;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image gatheringImage;

    @OneToMany(mappedBy = "gathering")
    List<Enrollment> enrollments = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    public void changeGathering(Image image, Category category, UpdateGatheringRequest updateGatheringRequest){
        if(image != null) this.gatheringImage = image;
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
                .count(1)
                .build();
    }

    public void enroll(List<Enrollment> enrollments){
        this.enrollments = enrollments;
    }
}
