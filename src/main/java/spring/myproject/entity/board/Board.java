package spring.myproject.entity.board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.user.User;

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
    @JoinColumn(name = "gathering_id")
    Gathering gathering;

    private String title;
    private String content;
    private String description;
    private LocalDateTime registerDate;
}
