package spring.myproject.dto.response.board;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import spring.myproject.dto.response.board.querydto.BoardQuery;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardResponse {
    private String code;
    private String message;
    private String title;
    private String description;
    private List<String> imageUrls;
    private String username;
    private String userImageUrl;
    private LocalDateTime registerDate;

    public static BoardResponse of(List<BoardQuery> boardQueries, List<String> imageUrls, String userImageUrl , String code, String message) {
        return BoardResponse.builder()
                .code(code)
                .message(message)
                .title(boardQueries.getFirst().getTitle())
                .description(boardQueries.getFirst().getDescription())
                .registerDate(boardQueries.getFirst().getRegisterDate())
                .imageUrls(imageUrls)
                .username(boardQueries.getFirst().getUsername())
                .userImageUrl(userImageUrl)
                .build();

    }
}
