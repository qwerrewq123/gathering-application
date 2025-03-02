package spring.myproject.domain.board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.meeting.Meeting;
import spring.myproject.domain.user.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    User user;
    @OneToOne
    @JoinColumn(name = "meeting_id")
    Meeting meeting;
    @OneToOne
    @JoinColumn(name = "Image_url")
    Image image;
    private String title;
    private String description;
    private LocalDateTime registerDate;
}
