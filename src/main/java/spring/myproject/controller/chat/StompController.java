package spring.myproject.controller.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import spring.myproject.rabbitmq.publisher.ChatPublisher;

import static spring.myproject.dto.request.chat.ChatRequestDto.*;

@Controller
@RequiredArgsConstructor
public class StompController {

    private final ChatPublisher chatPublisher;

    @MessageMapping("/room/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, @Payload ChatMessageRequest chatMessageRequest){
        chatPublisher.publish(roomId,chatMessageRequest);
    }
}
