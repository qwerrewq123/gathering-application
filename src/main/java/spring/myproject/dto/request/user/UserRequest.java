package spring.myproject.dto.request.user;

import lombok.Builder;
import lombok.Data;

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
