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

    @MessageMapping("/chatRoom/{chatRoomId}")
    public void sendMessage(@DestinationVariable Long chatRoomId, @Payload ChatMessageRequest chatMessageRequest){
        System.out.println(chatRoomId);
        System.out.println(chatMessageRequest.toString());
        chatPublisher.publish(chatRoomId,chatMessageRequest);
    }
}
