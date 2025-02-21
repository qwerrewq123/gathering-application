package spring.myproject.dto.response.meeting;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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
