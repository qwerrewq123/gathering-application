package spring.myproject.dto.response.enrollment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisEnrollGatheringResponse {
    private String code;
    private String message;

    public static DisEnrollGatheringResponse of(String code, String message) {
        return new DisEnrollGatheringResponse(code, message);
    }
}
