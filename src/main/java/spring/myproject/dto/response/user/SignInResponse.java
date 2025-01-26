package spring.myproject.dto.response.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInResponse {

    private String code;
    private String message;
    private String token;
}
