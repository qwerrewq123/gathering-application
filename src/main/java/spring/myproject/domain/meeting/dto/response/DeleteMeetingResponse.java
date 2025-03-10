package spring.myproject.domain.meeting.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteMeetingResponse {
    private String code;
    private String message;
    public static DeleteMeetingResponse of(String code, String message) {
        return new DeleteMeetingResponse(code, message);
    }
}
