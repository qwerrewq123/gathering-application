package spring.myproject.dto.response.gathering;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@Builder
public class GatheringQueryDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime registerDate;
    private String category;
    private String createdBy;
    private String participatedBy;
    private String url;
    private int count;
}
