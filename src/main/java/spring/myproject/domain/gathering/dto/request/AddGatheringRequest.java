package spring.myproject.domain.gathering.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AddGatheringRequest {

    private String title;
    private String content;
    private String category;


}
