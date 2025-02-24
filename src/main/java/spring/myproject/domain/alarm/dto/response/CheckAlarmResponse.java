package spring.myproject.domain.alarm.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckAlarmResponse {

    private String code;
    private String message;

    public static CheckAlarmResponse of(String code, String message){
        return new CheckAlarmResponse(code, message);
    }
}
