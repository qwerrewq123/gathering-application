package spring.myproject.dto.response.enrollment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisEnrollGatheringResponse {
    private String code;
    private String message;
}
