package spring.myproject.dto.response.like;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DislikeResponse {

    private String code;
    private String message;

    public static DislikeResponse of(String code, String message) {
        return new DislikeResponse(code, message);
    }
}
