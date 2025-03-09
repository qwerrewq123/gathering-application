package spring.myproject.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import spring.myproject.domain.chat.dto.request.ChatMessageRequest;
import spring.myproject.domain.chat.service.ChatService;
import spring.myproject.kafka.KafkaProducerService;

@Controller
@RequiredArgsConstructor
public class StompController {

    private final ChatService chatService;
    private final KafkaProducerService kafkaProducerService;

    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageRequest chatMessageRequest){

        kafkaProducerService.sendMessage("chat", chatMessageRequest);
        chatService.saveMessage(roomId,chatMessageRequest);
    }
}
