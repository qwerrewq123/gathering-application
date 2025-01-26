package spring.myproject.domain.like;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.client.HttpServerErrorException;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.user.User;
@Getter
@NoArgsConstructor
@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User likedBy;

    @OneToOne
    @JoinColumn(name = "gathering_id")
    private Gathering gatehring;
}
