package spring.myproject.dto.response.meeting;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteMeetingResponse {
    private String code;
    private String message;
}
