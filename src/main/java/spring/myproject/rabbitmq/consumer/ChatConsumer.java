package spring.myproject.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import spring.myproject.rabbitmq.event.Event;
import spring.myproject.rabbitmq.event.EventPayload;
import spring.myproject.rabbitmq.handler.EventHandler;

import java.util.List;

import static spring.myproject.config.RabbitMQConfig.*;

@Component
@RequiredArgsConstructor
public class ChatConsumer {
    private final List<EventHandler> eventHandlers;

    @RabbitListener(queues = CHAT_QUEUE)
    public void consumeChatMessage(String message){
        Event<EventPayload> event = Event.fromJson(message);
        EventHandler<EventPayload> eventHandler = eventHandlers.stream()
                .filter(handler -> handler.supports(event))
                .findFirst()
                .orElseThrow();
        eventHandler.handle(event);
    }
}
