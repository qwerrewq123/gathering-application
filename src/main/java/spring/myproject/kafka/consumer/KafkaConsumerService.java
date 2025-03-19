package spring.myproject.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import spring.myproject.kafka.event.Event;
import spring.myproject.kafka.event.EventPayload;
import spring.myproject.kafka.event.EventType;
import spring.myproject.kafka.handler.EventHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final List<EventHandler> eventHandlers;

    @KafkaListener(topics = EventType.Topic.GATHERING_CHAT, groupId = "chat-group")
    public void listenChat(String message, Acknowledgment ack) {
        Event<EventPayload> event = Event.fromJson(message);
        EventHandler<EventPayload> eventHandler = eventHandlers.stream()
                .filter(handler -> handler.supports(event))
                .findFirst()
                .get();
        eventHandler.handle(event);
        ack.acknowledge();
    }
    

}
