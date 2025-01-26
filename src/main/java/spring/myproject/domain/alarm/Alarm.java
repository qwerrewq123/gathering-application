package spring.myproject.domain.alarm;

import jakarta.persistence.*;
import lombok.*;
import spring.myproject.domain.user.User;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
@Entity
@Setter
@AllArgsConstructor
@Builder
@Table(name = "alarm")
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime date;
    private Boolean checked;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
