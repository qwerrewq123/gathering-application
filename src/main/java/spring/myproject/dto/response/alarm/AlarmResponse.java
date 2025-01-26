package spring.myproject.dto.response.alarm;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AlarmResponse {

    private String content;
    private LocalDateTime date;
    private Boolean checked;
}
