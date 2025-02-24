package spring.myproject.domain.attend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddAttendResponse {
    private String code;
    private String message;

    public static AddAttendResponse of(String code, String message) {
        return new AddAttendResponse(code, message);
    }
}
