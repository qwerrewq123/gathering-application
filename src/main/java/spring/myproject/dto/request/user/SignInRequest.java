package spring.myproject.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SignInRequest {

    private String username;
    private String password;
}
