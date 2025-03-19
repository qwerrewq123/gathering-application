package spring.myproject.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import spring.myproject.kafka.event.Event;
import spring.myproject.kafka.event.EventPayload;
import spring.myproject.kafka.event.EventType;
import spring.myproject.kafka.event.handler.EventHandler;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final SimpMessageSendingOperations messageTemplate;
    private final EventHandler eventHandler;
    @KafkaListener(topics = EventType.Topic.GATHERING_CHAT, groupId = "chat-group")
    public void listenChat(String message, Acknowledgment ack) {
        Event<EventPayload> event = Event.fromJson(message);
        eventHandler.handle(event);
    }
    

}
