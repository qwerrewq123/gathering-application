package spring.myproject.dto.request.gathering;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AddGatheringRequest {

    private String title;
    private String content;
    private LocalDateTime registerDate;
    private String category;
    private String createdBy;

}
