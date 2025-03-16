package spring.myproject.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fail {

    @Id
    private Long id;
    private String content;
    private String clientId;

    public static Fail of(String clientId, String content) {
        return Fail.builder()
                .clientId(clientId)
                .content(content)
                .build();
    }


}
