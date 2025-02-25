package spring.myproject.domain.gathering;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@Entity
@Setter
@AllArgsConstructor
@Builder
@Table(name = "gathering_count")
public class GatheringCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int count;
}
