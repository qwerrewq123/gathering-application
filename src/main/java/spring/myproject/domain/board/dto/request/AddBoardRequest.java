package spring.myproject.domain.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.myproject.domain.board.Board;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.meeting.Meeting;
import spring.myproject.domain.user.User;

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
