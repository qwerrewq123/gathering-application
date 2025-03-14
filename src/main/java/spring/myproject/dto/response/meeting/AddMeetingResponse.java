package spring.myproject.dto.response.meeting;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddMeetingResponse {
    private String code;
    private String message;

    public static AddMeetingResponse of(String code, String message) {
        return new AddMeetingResponse(code, message);
    }
}
