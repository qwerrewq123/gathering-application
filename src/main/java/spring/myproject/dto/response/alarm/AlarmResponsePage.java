package spring.myproject.dto.response.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@Data
@Builder
public class AlarmResponsePage {

    private String code;
    private String message;
    private Page<AlarmResponse> page;

}
