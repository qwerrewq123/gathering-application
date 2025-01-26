package spring.myproject.dto.response.gathering;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class GatheringReponse {

    private String title;
    private String content;
    private LocalDateTime registerDate;
    private String category;
    private String createdBy;
    private List<String> participatedBy;
    private String image;
}
