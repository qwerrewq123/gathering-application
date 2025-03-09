package spring.myproject.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import spring.myproject.domain.chat.dto.request.ChatMessageRequest;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final SimpMessageSendingOperations messageTemplate;

    @KafkaListener(topics = "chat", groupId = "stomp-group")
    public void listenChat(ChatMessageRequest chatMessageRequest) {
        messageTemplate.convertAndSend("/topic/"+chatMessageRequest.getRoomId(), chatMessageRequest);
    }
    

}
