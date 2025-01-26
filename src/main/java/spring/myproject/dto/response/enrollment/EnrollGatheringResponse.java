package spring.myproject.dto.response.enrollment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollGatheringResponse {
    private String code;
    private String message;
}
