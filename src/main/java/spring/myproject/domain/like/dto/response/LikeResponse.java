package spring.myproject.domain.like.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeResponse {

    private String code;
    private String message;

    public static LikeResponse of(String code, String message) {
        return new LikeResponse(code, message);
    }
}
