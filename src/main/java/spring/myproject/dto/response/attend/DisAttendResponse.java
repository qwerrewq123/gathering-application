package spring.myproject.dto.response.attend;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisAttendResponse {
    private String code;
    private String message;

    public static DisAttendResponse of(String code, String message) {
        return new DisAttendResponse(code, message);
    }
}
