package spring.myproject.rabbitmq.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.rabbitmq.event.EventPayload;

import static spring.myproject.dto.request.chat.ChatRequestDto.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SendChatMessageEventPayload implements EventPayload {

    private Long roomId;
    private String content;
    private Long userId;

    public static SendChatMessageEventPayload of(Long roomId,ChatMessageRequest chatMessageRequest) {
        return SendChatMessageEventPayload.builder()
                .roomId(roomId)
                .content(chatMessageRequest.getContent())
                .userId(chatMessageRequest.getUserId())
                .build();
    }
}
