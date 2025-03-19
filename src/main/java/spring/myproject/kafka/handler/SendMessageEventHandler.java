package spring.myproject.kafka.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import spring.myproject.async.AsyncService;
import spring.myproject.kafka.event.Event;
import spring.myproject.kafka.event.EventType;
import spring.myproject.kafka.payload.SendChatMessageEventPayload;
import spring.myproject.repository.chat.ChatMessageRepository;
import spring.myproject.repository.chat.ReadStatusRepository;

@Component
@RequiredArgsConstructor
public class SendMessageEventHandler implements EventHandler<SendChatMessageEventPayload>{

    private final SimpMessageSendingOperations messageTemplate;
    private final AsyncService asyncService;
    @Override
    public void handle(Event<SendChatMessageEventPayload> event) {
        SendChatMessageEventPayload payload = event.getPayload();
        messageTemplate.convertAndSend("/topic/"+payload.getRoomId(), payload.getContent());
        asyncService.insertChatMessageAndReadStatus(event);
    }

    @Override
    public boolean supports(Event event) {
        return EventType.SEND_MESSAGE == event.getType();
    }
}
