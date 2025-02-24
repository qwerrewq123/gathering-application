package spring.myproject.domain.alarm.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddAlarmResponse {

    private String code;
    private String message;

    public static AddAlarmResponse of(String code, String message){
        return new AddAlarmResponse(code, message);
    }
}
