package spring.myproject.domain.fail;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.domain.user.User;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fail {

    @Id
    private Long id;
    private String message;
    private Boolean processed;
    @OneToOne(optional = false)
    private User user;

    public static Fail of(User user, String message) {
        return Fail.builder()
                .message(message)
                .user(user)
                .processed(false)
                .build();
    }
}
