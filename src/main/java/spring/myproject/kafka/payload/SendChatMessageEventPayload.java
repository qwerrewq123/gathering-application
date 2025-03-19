package spring.myproject.kafka.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import spring.myproject.kafka.event.EventPayload;
@Getter
public class SendChatMessageEventPayload implements EventPayload {

    private Long roomId;
    private String content;
    private String username;
}
