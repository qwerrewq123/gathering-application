package spring.myproject.dto.response.chat.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageElement {
    private Long id;
    private Long chatRoomId;
    private String content;
    private Long senderId;
    private String senderUsername;
    @JsonProperty("isRead")
    private boolean isRead;
    @JsonProperty("isMe")
    private boolean isMe;
    private LocalDateTime createdAt;
}
