package spring.myproject.dto.request.board;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.myproject.entity.board.Board;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.user.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddBoardRequest {
    @NotBlank(message = "cannot blank or null or space")
    private String title;
    @NotBlank(message = "cannot blank or null or space")
    private String description;

    public static Board of(AddBoardRequest addBoardRequest, User user, Gathering gathering) {
        return Board.builder()
                .title(addBoardRequest.getTitle())
                .description(addBoardRequest.getDescription())
                .user(user)
                .gathering(gathering)
                .registerDate(LocalDateTime.now())
                .build();
    }
}
