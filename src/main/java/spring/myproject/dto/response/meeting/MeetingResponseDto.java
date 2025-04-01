package spring.myproject.dto.response.meeting;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import spring.myproject.dto.response.meeting.querydto.MeetingDetailQuery;

import java.time.LocalDateTime;
import java.util.List;

public class MeetingResponseDto {
    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AddMeetingResponse {
        private String code;
        private String message;
        private Long id;

        public static AddMeetingResponse of(String code, String message,Long id) {
            return new AddMeetingResponse(code, message,id);
        }

    }
    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UpdateMeetingResponse {
        private String code;
        private String message;
        private Long id;
        public static UpdateMeetingResponse of(String code, String message,Long id) {
            return new UpdateMeetingResponse(code, message,id);
        }
    }
    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DeleteMeetingResponse {
        private String code;
        private String message;
        public static DeleteMeetingResponse of(String code, String message) {
            return new DeleteMeetingResponse(code, message);
        }

    }
    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MeetingResponse {

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

        public static MeetingResponse of(String code, String message, List<MeetingDetailQuery> meetingDetailQueries, List<String> attends, String url){
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

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MeetingsResponse {

        private String code;
        private String message;
        private List<MeetingElement> content;
        boolean hasNext;
        public static MeetingsResponse of(String code, String message,List<MeetingElement> content,boolean hasNext) {
            return new MeetingsResponse(code, message,content,hasNext);
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MeetingElement {

        private Long id;
        private String title;
        private String createdBy;
        private LocalDateTime boardDate;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String content;
        private int count;
        private String url;
    }

}
