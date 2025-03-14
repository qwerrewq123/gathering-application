package spring.myproject.dto.request.meeting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class AddMeetingRequest {
    @NotBlank(message = "cannot blank or null or space")
    private String title;
    @NotBlank(message = "cannot blank or null or space")
    private String content;
    @NotNull(message = "cannot null")
    private LocalDateTime startDate;
    @NotNull(message = "cannot null")
    private LocalDateTime endDate;


}
