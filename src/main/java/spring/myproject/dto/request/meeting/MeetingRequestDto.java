package spring.myproject.dto.request.meeting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MeetingRequestDto {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddMeetingRequest {
        @NotBlank(message = "cannot blank or null or space")
        private String title;
        @NotBlank(message = "cannot blank or null or space")
        private String content;
        @NotNull(message = "cannot null")
        private LocalDateTime startDate;
        @NotNull(message = "cannot null")
        private LocalDateTime endDate;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class UpdateMeetingRequest {
        private String title;
        private String content;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }
}
