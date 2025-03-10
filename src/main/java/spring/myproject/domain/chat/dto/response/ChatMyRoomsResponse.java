package spring.myproject.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMyRoomsResponse {
    private String code;
    private String message;
    private Page<ChatMyRoomResponse> page;

    public static ChatMyRoomsResponse of(String code, String message, Page<ChatMyRoomResponse> page) {
        return new ChatMyRoomsResponse(code, message, page);
    }
}
