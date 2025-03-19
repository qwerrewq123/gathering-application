package spring.myproject.controller.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import spring.myproject.dto.request.chat.ChatMessageRequest;
import spring.myproject.kafka.event.EventType;
import spring.myproject.service.chat.ChatService;
import spring.myproject.kafka.producer.KafkaProducerService;

@Controller
@RequiredArgsConstructor
public class StompController {

    private final KafkaProducerService kafkaProducerService;

    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageRequest chatMessageRequest){
        kafkaProducerService.publishSendMessageEvent(EventType.Topic.GATHERING_CHAT, chatMessageRequest);
    }
}
