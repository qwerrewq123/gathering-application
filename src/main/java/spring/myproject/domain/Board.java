package spring.myproject.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
