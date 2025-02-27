package spring.myproject.domain.meeting;

import jakarta.persistence.*;
import lombok.*;
import spring.myproject.domain.attend.Attend;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDateTime boardDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createdBy;
    @OneToMany(mappedBy = "meeting")
    private List<Attend> attends = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private Gathering gathering;
    @OneToOne
    @JoinColumn(name = "meeting_image")
    private Image image;

    public void attend(List<Attend> attends){
        for (Attend attend : attends) {
            attend.addMeeting(this);
        }
        this.attends = attends;
    }
}
