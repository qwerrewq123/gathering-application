package spring.myproject.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NicknameCheckRequest {
    @NotBlank(message = "cannot blank or null or space")
    private String nickname;
}
