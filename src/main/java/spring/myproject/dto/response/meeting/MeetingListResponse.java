package spring.myproject.dto.response.meeting;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
public class MeetingListResponse {

    private String code;
    private String message;
    private Page<MeetingQueryListResponse> page;

}
