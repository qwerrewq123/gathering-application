package spring.myproject.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EmailCertificationRequest {
    @NotBlank(message = "cannot blank or null or space")
    private String clientId;
    @NotBlank(message = "cannot blank or null or space")
    private String email;
}
