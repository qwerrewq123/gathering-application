package spring.myproject.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import spring.myproject.kafka.event.Event;
import spring.myproject.kafka.event.EventPayload;
import spring.myproject.kafka.event.EventType;
import spring.myproject.kafka.payload.SendChatMessageEventPayload;

import java.util.concurrent.atomic.AtomicLong;

import static spring.myproject.dto.request.chat.ChatRequestDto.*;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private static final AtomicLong counter = new AtomicLong(0);
    private final KafkaTemplate<String, String> kafkaTemplate;
    public void publishSendMessageEvent(String topic, Long roomId,ChatMessageRequest chatMessageRequest) {
        SendChatMessageEventPayload payload = SendChatMessageEventPayload.of(roomId,chatMessageRequest);
        Event<EventPayload> event = Event.of(fetchEventId(), EventType.SEND_MESSAGE, payload);
        kafkaTemplate.send(topic, event.toJson());
    }
    public Long fetchEventId() {
        return counter.incrementAndGet();
    }
}
