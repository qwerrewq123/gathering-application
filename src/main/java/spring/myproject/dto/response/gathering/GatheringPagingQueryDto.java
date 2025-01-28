package spring.myproject.dto.response.gathering;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class GatheringPagingQueryDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime registerDate;
    private String category;
    private String createdBy;
    private String url;
    private int count;
}
