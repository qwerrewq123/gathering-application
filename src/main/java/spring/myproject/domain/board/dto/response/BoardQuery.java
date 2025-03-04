package spring.myproject.domain.board.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import spring.myproject.domain.board.Board;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
