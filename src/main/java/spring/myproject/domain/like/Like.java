package spring.myproject.domain.like;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.user.User;
@Getter
@NoArgsConstructor
@Entity
@Table(name = "likes")
@AllArgsConstructor
@Builder
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User likedBy;

    @OneToOne
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;
}
