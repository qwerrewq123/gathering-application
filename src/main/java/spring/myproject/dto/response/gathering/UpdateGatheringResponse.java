package spring.myproject.dto.response.gathering;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateGatheringResponse {
    private String code;
    private String message;
}
