package spring.myproject.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import spring.myproject.dto.request.chat.ChatMessageRequest;
import spring.myproject.kafka.event.Event;
import spring.myproject.kafka.event.EventPayload;
import spring.myproject.kafka.event.EventType;
import spring.myproject.kafka.payload.SendChatMessageEventPayload;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private static final AtomicLong counter = new AtomicLong(0);
    private final KafkaTemplate<String, String> kafkaTemplate;
    public void publishSendMessageEvent(String topic, ChatMessageRequest chatMessageRequest) {
        SendChatMessageEventPayload payload = SendChatMessageEventPayload.of(chatMessageRequest);
        Event<EventPayload> event = Event.of(fetchEventId(), EventType.SEND_MESSAGE, payload);
        kafkaTemplate.send(topic, event.toJson());
    }
    public Long fetchEventId() {
        return counter.incrementAndGet();
    }
}
