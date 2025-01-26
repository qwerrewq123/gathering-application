package spring.myproject.dto.request.user;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;
import spring.myproject.domain.image.Image;

@Data
@Builder
public class UserRequest {

    private String username;
    private String password;
    private String email;
    private String address;
    private Integer age;
    private String hobby;

}
