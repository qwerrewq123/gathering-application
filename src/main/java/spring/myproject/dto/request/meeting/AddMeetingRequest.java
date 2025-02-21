package spring.myproject.dto.request.meeting;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import spring.myproject.domain.user.User;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class AddMeetingRequest {

    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;


}
