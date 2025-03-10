package spring.myproject.domain.gathering.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UpdateGatheringRequest {
    @NotBlank(message = "cannot blank or null or space")
    private String title;
    @NotBlank(message = "cannot blank or null or space")
    private String content;
    @NotBlank(message = "cannot blank or null or space")
    private String category;

}
