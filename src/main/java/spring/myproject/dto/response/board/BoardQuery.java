package spring.myproject.dto.response.board;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardQuery {
    private String title;
    private String description;
    private String imageUrl;
    private String username;
    private String userImageUrl;
    private LocalDateTime registerDate;
}
