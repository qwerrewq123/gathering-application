package spring.myproject.dto.response.meeting;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MeetingQueryListResponse {

    private Long id;
    private String title;
    private String createdBy;
    private LocalDateTime boardDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
}
