package spring.myproject.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import spring.myproject.dto.request.chat.ChatMessageRequest;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, ChatMessageRequest> kafkaTemplate;

    public void sendMessage(String topic, ChatMessageRequest chatMessageRequest) {
        kafkaTemplate.send(topic, chatMessageRequest);
    }
}
