package spring.myproject.dto.request.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInRequest {

    private String username;
    private String password;
}
