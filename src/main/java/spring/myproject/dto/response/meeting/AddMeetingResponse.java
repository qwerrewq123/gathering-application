package spring.myproject.dto.response.meeting;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddMeetingResponse {
    private String code;
    private String message;
}
