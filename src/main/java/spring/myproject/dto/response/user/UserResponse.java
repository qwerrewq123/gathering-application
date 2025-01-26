package spring.myproject.dto.response.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private String username;
    private String password;
    private String email;
    private String address;
    private Integer age;
    private String hobby;
    private String image;

}
