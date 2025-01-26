package spring.myproject.dto.request.alarm;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddAlarmRequest {

    private String content;
}
