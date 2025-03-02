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
public class BoardResponse {
    private String code;
    private String message;
    private String title;
    private String description;
    private List<String> imageUrls;
    private String username;
    private String userImageUrl;
    private LocalDateTime registerDate;
    public static BoardResponse of(Board fetchBoard, String code, String message) {
        return BoardResponse.builder()
                .code(code)
                .message(message)
                .title(fetchBoard.getTitle())
                .description(fetchBoard.getDescription())
                .registerDate(fetchBoard.getRegisterDate())
                .imageUrls(new ArrayList<>())
                .username(fetchBoard.getUser().getUsername())
                .userImageUrl(fetchBoard.getUser().getProfileImage().getUrl())
                .build();

    }
}
