package spring.myproject.domain.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.myproject.domain.board.Board;
import spring.myproject.domain.meeting.Meeting;
import spring.myproject.domain.user.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddBoardRequest {

    private String title;
    private String description;

    public static Board of(AddBoardRequest addBoardRequest, User user, Meeting meeting) {
        return Board.builder()
                .title(addBoardRequest.getTitle())
                .description(addBoardRequest.getDescription())
                .user(user)
                .meeting(meeting)
                .registerDate(LocalDateTime.now())
                .build();
    }
}
