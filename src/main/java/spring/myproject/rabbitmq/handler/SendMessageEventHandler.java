package spring.myproject.rabbitmq.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import spring.myproject.common.async.AsyncService;
import spring.myproject.rabbitmq.event.Event;
import spring.myproject.rabbitmq.event.EventType;
import spring.myproject.rabbitmq.payload.SendChatMessageEventPayload;

@Component
@RequiredArgsConstructor
public class SendMessageEventHandler implements EventHandler<SendChatMessageEventPayload>{

    private final SimpMessageSendingOperations messageTemplate;
    private final AsyncService asyncService;
    @Override
    public void handle(Event<SendChatMessageEventPayload> event) {
        SendChatMessageEventPayload payload = event.getPayload();
        messageTemplate.convertAndSend("/chatRoom/"+payload.getRoomId(), SendChatMessageEventPayload.from(payload));
//        asyncService.insertChatMessageAndReadStatus(event);
    }

    @Override
    public boolean supports(Event event) {
        return EventType.SEND_MESSAGE == event.getType();
    }
}
