package spring.myproject.dto.request.meeting;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateMeetingRequest {

    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;


}
