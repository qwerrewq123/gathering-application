package spring.myproject.dto.response.meeting;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeetingResponse {

    private String code;
    private String message;
    private Long id;
    private String title;
    private String createdBy;
    private List<String> attendedBy;
    private LocalDateTime boardDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
    private String url;

    public static MeetingResponse of(String code, String message, List<MeetingDetailQuery> meetingDetailQueries,List<String> attends, String url){
        return MeetingResponse.builder()
                .code(code)
                .message(message)
                .id(meetingDetailQueries.getLast().getId())
                .title(meetingDetailQueries.getFirst().getTitle())
                .content(meetingDetailQueries.getFirst().getContent())
                .boardDate(meetingDetailQueries.getFirst().getBoardDate())
                .startDate(meetingDetailQueries.getFirst().getStartDate())
                .endDate(meetingDetailQueries.getFirst().getEndDate())
                .createdBy(meetingDetailQueries.getFirst().getCreatedBy())
                .attendedBy(attends)
                .url(url)
                .build();
    }
}
