package spring.myproject.kafka.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.dto.request.chat.ChatMessageRequest;
import spring.myproject.kafka.event.EventPayload;
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SendChatMessageEventPayload implements EventPayload {

    private Long roomId;
    private String content;
    private String username;

    public static SendChatMessageEventPayload of(ChatMessageRequest chatMessageRequest) {
        return SendChatMessageEventPayload.builder()
                .roomId(chatMessageRequest.getRoomId())
                .content(chatMessageRequest.getContent())
                .username(chatMessageRequest.getUsername())
                .build();
    }
}
