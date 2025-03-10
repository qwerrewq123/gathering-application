package spring.myproject.domain.meeting.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeetingsResponse {

    private String code;
    private String message;
    private Page<MeetingsQuery> page;
    public static MeetingsResponse of(String code, String message,Page<MeetingsQuery> page) {
        return new MeetingsResponse(code, message,page);
    }

}
