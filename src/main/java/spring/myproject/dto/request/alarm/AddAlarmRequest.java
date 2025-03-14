package spring.myproject.dto.request.alarm;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AddAlarmRequest {

    @NotBlank(message = "cannot blank or null or space")
    private String content;
}
