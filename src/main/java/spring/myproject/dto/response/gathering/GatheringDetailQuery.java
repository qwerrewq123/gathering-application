package spring.myproject.dto.response.gathering;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GatheringDetailQuery {

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
