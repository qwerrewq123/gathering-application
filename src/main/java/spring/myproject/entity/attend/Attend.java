package spring.myproject.entity.attend;

import jakarta.persistence.*;
import lombok.*;
import spring.myproject.entity.meeting.Meeting;
import spring.myproject.entity.user.User;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Attend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean accepted;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User attendBy;
    private LocalDateTime date;
    public static Attend of(boolean accepted, Meeting meeting, User attendBy, LocalDateTime date) {
        return Attend.builder()
                .accepted(accepted)
                .meeting(meeting)
                .attendBy(attendBy)
                .date(date)
                .build();
    }
    public void changeAccepted(boolean accepted) {
        this.accepted = accepted;
    }
    public void addMeeting(Meeting meeting){
        this.meeting = meeting;
    }
    public static Attend of(Meeting meeting, User user){
        return Attend.builder()
                .meeting(meeting)
                .date(LocalDateTime.now())
                .accepted(true)
                .attendBy(user)
                .build();
    }

}
