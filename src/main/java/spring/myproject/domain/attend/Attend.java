package spring.myproject.domain.attend;

import jakarta.persistence.*;
import lombok.*;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.meeting.Meeting;
import spring.myproject.domain.user.User;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Attend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean accepted;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User attendBy;

    private LocalDateTime date;

}
