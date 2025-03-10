package spring.myproject.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SignInRequest {
    @NotBlank(message = "cannot blank or null or space")
    private String username;
    @NotBlank(message = "cannot blank or null or space")
    private String password;
}
