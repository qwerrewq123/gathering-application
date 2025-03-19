package spring.myproject.kafka.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import spring.myproject.kafka.event.Event;
import spring.myproject.kafka.event.EventType;
import spring.myproject.kafka.payload.SendChatMessageEventPayload;
import spring.myproject.repository.chat.ChatMessageRepository;
import spring.myproject.repository.chat.ReadStatusRepository;

@Component
@RequiredArgsConstructor
public class SendMessageEventHandler implements EventHandler<SendChatMessageEventPayload>{
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final SimpMessageSendingOperations messageTemplate;

    @Override
    public void handle(Event<SendChatMessageEventPayload> event) {
        SendChatMessageEventPayload payload = event.getPayload();
        messageTemplate.convertAndSend("/topic/"+payload.getChatRoomId(), payload.getContent());
        //TODO : chatMessaging 저장, ReadStatus 저장
    }

    @Override
    public boolean supports(Event event) {
        return EventType.SEND_MESSAGE == event.getType();
    }
}
