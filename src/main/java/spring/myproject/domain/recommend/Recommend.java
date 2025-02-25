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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    private Long count;

    private void reset(Recommend recommend){
        this.count = 0L;
    }
}
