package spring.myproject.domain.recommend;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.domain.gathering.Gathering;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "recommend")
public class Recommend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

}
