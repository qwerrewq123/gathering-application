package spring.myproject.domain.gathering.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateGatheringResponse {
    private String code;
    private String message;

    public static UpdateGatheringResponse of(String code, String message) {
        return new UpdateGatheringResponse(code, message);
    }
}
