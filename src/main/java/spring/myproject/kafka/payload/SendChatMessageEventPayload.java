package spring.myproject.kafka.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.kafka.event.EventPayload;

import static spring.myproject.dto.request.chat.ChatRequestDto.*;

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
