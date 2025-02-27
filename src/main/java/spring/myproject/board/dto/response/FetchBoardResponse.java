package spring.myproject.board.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import spring.myproject.board.Board;
import spring.myproject.domain.user.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FetchBoardResponse {
    private String code;
    private String message;
    private String title;
    private String description;
    private String imageUrl;
    private String username;
    private String userImageUrl;
    private LocalDateTime registerDate;
    public static FetchBoardResponse of(Board fetchBoard,String code,String message) {
        return FetchBoardResponse.builder()
                .code(code)
                .message(message)
                .title(fetchBoard.getTitle())
                .description(fetchBoard.getDescription())
                .registerDate(fetchBoard.getRegisterDate())
                .imageUrl(fetchBoard.getImage().getUrl())
                .username(fetchBoard.getUser().getUsername())
                .userImageUrl(fetchBoard.getUser().getProfileImage().getUrl())
                .build();

    }
}
