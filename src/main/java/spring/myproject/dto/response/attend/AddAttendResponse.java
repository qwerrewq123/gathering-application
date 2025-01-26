package spring.myproject.dto.response.attend;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddAttendResponse {
    private String code;
    private String message;
}
