package spring.myproject.domain.alarm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
