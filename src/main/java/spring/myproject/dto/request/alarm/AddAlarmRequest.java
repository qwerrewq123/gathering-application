package spring.myproject.dto.request.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AddAlarmRequest {

    private String content;
}
