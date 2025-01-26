package spring.myproject.dto.response.attend;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermitAttendResponse {
    private String code;
    private String message;
}
