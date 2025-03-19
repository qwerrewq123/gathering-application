package spring.myproject.kafka.payload;

import lombok.Getter;
import spring.myproject.kafka.event.EventPayload;
@Getter
public class SendChatMessageEventPayload implements EventPayload {

    private Long chatRoomId;
    private Long chatParticipantId;
    private String content;

}
