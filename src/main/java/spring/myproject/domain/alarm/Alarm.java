package spring.myproject.domain.alarm;

import jakarta.persistence.*;
import lombok.*;
import spring.myproject.domain.alarm.dto.request.AddAlarmRequest;
import spring.myproject.domain.user.User;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@Entity
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Alarm of(AddAlarmRequest addAlarmRequest, LocalDateTime date, Boolean checked, User user) {
        return Alarm.builder()
                .content(addAlarmRequest.getContent())
                .date(date)
                .checked(checked)
                .user(user)
                .build();
    }
}
