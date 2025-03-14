package spring.myproject.dto.response.alarm;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteAlarmResponse {

    private String code;
    private String message;

    public static DeleteAlarmResponse of(String code, String message) {
        return new DeleteAlarmResponse(code, message);
    }
}
