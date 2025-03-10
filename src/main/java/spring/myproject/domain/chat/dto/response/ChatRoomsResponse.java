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
public class ChatRoomsResponse {
    private String code;
    private String message;
    private Page<ChatRoomResponse> page;

    public static ChatRoomsResponse of(String code, String message, Page<ChatRoomResponse> page) {
        return new ChatRoomsResponse(code, message, page);
    }
}
