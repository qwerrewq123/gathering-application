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
    private String clientId;

    public static Fail of(String clientId, String message) {
        return Fail.builder()
                .message(message)
                .clientId(clientId)
                .processed(false)
                .build();
    }
}
