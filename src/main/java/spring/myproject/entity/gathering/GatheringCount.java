package spring.myproject.entity.gathering;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "gathering")
@AllArgsConstructor
@Builder
public class GatheringCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long count;
    @OneToOne(optional = false)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;
}
