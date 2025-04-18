package spring.myproject.rabbitmq.handler;

import spring.myproject.rabbitmq.event.Event;
import spring.myproject.rabbitmq.event.EventPayload;

public interface EventHandler<T extends EventPayload> {
    void handle(Event<T> event);
    boolean supports(Event<T> event);
}
