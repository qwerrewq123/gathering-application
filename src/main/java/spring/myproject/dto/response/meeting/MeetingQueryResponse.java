package spring.myproject.dto.response.meeting;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
public class MeetingQueryResponse {

    private Long id;
    private String title;
    private String createdBy;
    private String attendedBy;
    private LocalDateTime boardDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
}
