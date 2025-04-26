package spring.myproject.rabbitmq.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import spring.myproject.dto.request.chat.ChatRequestDto;
import spring.myproject.rabbitmq.event.Event;
import spring.myproject.rabbitmq.event.EventPayload;
import spring.myproject.rabbitmq.event.EventType;
import spring.myproject.rabbitmq.payload.SendChatMessageEventPayload;

import static spring.myproject.config.RabbitMQConfig.*;
import static spring.myproject.dto.request.chat.ChatRequestDto.*;

@Component
@RequiredArgsConstructor
public class ChatPublisher {
    private final RabbitTemplate rabbitTemplate;
    public void publish(Long chatRoomId, ChatMessageRequest chatMessageRequest) {
        SendChatMessageEventPayload payload = SendChatMessageEventPayload.of(chatRoomId,chatMessageRequest);
        Event<EventPayload> event = Event.of(EventType.SEND_MESSAGE, payload);
        String message = event.toJson();
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, CHAT_ROUTING_KEY, message);
    }
}
