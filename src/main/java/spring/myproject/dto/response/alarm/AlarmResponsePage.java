package spring.myproject.dto.response.alarm;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlarmResponsePage {

    private String code;
    private String message;
    private Page<AlarmResponse> page;

}
