package spring.myproject.rabbitmq.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.myproject.rabbitmq.payload.SendChatMessageEventPayload;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {
    SEND_MESSAGE(SendChatMessageEventPayload.class, Topic.GATHERING_CHAT);

    private final Class<? extends EventPayload> payloadClass;
    private final String topic;

    public static EventType from(String type) {
        try {
            return valueOf(type);
        } catch (Exception e) {
            log.error("[EventType.from] type={}", type, e);
            return null;
        }
    }

    public static class Topic {
        public static final String GATHERING_CHAT = "gathering-chat";
    }
}
