package spring.myproject.dto.response.alarm;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckAlarmResponse {

    private String code;
    private String message;
}
