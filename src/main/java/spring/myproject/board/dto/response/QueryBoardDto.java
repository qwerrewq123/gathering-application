package spring.myproject.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryBoardDto {
    private String title;
    private String description;
    private String imageUrl;
    private String username;
    private String userImageUrl;
    private LocalDateTime registerDate;
}
