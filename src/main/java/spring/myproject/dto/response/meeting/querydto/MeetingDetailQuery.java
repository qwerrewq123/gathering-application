package spring.myproject.dto.response.meeting.querydto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeetingDetailQuery {

    private Long id;
    private String title;
    private String createdBy;
    private String createdByNickname;
    private String createdByUrl;
    private String attendedBy;
    private String attendByNickname;
    private String attendedByUrl;
    private LocalDateTime boardDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
    private int count;
    private String url;
}
