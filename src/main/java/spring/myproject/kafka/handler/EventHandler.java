package spring.myproject.kafka.handler;

import spring.myproject.kafka.event.Event;
import spring.myproject.kafka.event.EventPayload;

public interface EventHandler<T extends EventPayload> {
    void handle(Event<T> event);
    boolean supports(Event<T> event);
}
