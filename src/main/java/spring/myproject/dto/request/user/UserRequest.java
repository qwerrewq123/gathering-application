package spring.myproject.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "cannot blank or null or space")
    private String username;
    @NotBlank(message = "cannot blank or null or space")
    private String password;
    @NotBlank(message = "cannot blank or null or space")
    private String email;
    @NotBlank(message = "cannot blank or null or space")
    private String address;
    @NotNull(message = "cannot null")
    private Integer age;
    @NotBlank(message = "cannot blank or null or space")
    private String hobby;
    @NotBlank(message = "cannot blank or null or space")
    private String nickname;

}
