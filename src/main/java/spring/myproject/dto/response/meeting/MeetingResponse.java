package spring.myproject.dto.response.meeting;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import spring.myproject.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class MeetingResponse {

    private String code;
    private String message;
    private String title;
    private String createdBy;
    private List<String> attendedBy;
    private LocalDateTime boardDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
}
