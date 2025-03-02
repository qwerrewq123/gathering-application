package spring.myproject.domain.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardsQuery {
    private String title;
    private String description;
    private String username;
    private String content;
    private LocalDateTime registerDate;
}
