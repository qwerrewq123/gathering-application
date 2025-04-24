package spring.myproject.dto.response.chat.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageElement {
    private Long chatRoomId;
    private Long chatMessageId;
    private String content;
    private String username;
    private boolean status;
}
