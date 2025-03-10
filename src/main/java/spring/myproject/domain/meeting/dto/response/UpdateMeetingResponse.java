package spring.myproject.domain.meeting.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateMeetingResponse {
    private String code;
    private String message;
    public static UpdateMeetingResponse of(String code, String message) {
        return new UpdateMeetingResponse(code, message);
    }
}
