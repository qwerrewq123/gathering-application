package spring.myproject.entity.fcm;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.entity.gathering.Gathering;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String topicName;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "topic", optional = false)
    private Gathering gathering;

}

