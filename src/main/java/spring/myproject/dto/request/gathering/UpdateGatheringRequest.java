package spring.myproject.dto.request.gathering;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class UpdateGatheringRequest {

    private String title;
    private String content;
    private String category;

}
