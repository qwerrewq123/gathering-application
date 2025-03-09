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
}
