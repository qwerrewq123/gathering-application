package spring.myproject.domain.alarm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AddAlarmRequest {

    private String content;
}
