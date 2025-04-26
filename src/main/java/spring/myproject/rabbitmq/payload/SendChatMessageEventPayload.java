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
    private String username;

    public static SendChatMessageEventPayload of(Long chatRoomId,ChatMessageRequest chatMessageRequest) {
        return SendChatMessageEventPayload.builder()
                .roomId(chatRoomId)
                .content(chatMessageRequest.getContent())
                .userId(chatMessageRequest.getUserId())
                .username(chatMessageRequest.getUsername())
                .build();
    }

    public static ChatMessageResponse from(SendChatMessageEventPayload payload) {
        return ChatMessageResponse.builder()
                .content(payload.getContent())
                .userId(payload.getUserId())
                .username(payload.getUsername())
                .build();
    }


}
