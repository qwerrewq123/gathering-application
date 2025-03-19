package spring.myproject.async;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.entity.chat.ChatMessage;
import spring.myproject.entity.chat.ChatParticipant;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.entity.chat.ReadStatus;
import spring.myproject.entity.user.User;
import spring.myproject.exception.chat.NotFoundChatParticipantException;
import spring.myproject.exception.chat.NotFoundChatRoomException;
import spring.myproject.exception.user.NotFoundUserException;
import spring.myproject.kafka.event.Event;
import spring.myproject.kafka.event.EventPayload;
import spring.myproject.kafka.payload.SendChatMessageEventPayload;
import spring.myproject.repository.chat.ChatMessageRepository;
import spring.myproject.repository.chat.ChatParticipantRepository;
import spring.myproject.repository.chat.ChatRoomRepository;
import spring.myproject.repository.chat.ReadStatusRepository;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.service.fail.FailService;
import spring.myproject.dto.request.user.EmailCertificationRequest;
import spring.myproject.provider.EmailProvider;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AsyncService {

    private final EmailProvider emailProvider;
    private final FailService failService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final UserRepository userRepository;

    @Async("customAsyncExecutor")
    public void asyncTask(EmailCertificationRequest emailCertificationRequest){
        try {
            emailCertification(emailCertificationRequest);
        }catch (MessagingException e){
            failService.send(emailCertificationRequest.getClientId());
        }
    }

    @Async("customAsyncExecutor")
    @Transactional
    public void insertChatMessageAndReadStatus(Event<SendChatMessageEventPayload> event){
        SendChatMessageEventPayload payload = event.getPayload();
        User user = userRepository.findByUsername(payload.getUsername())
                .orElseThrow(()-> new NotFoundUserException("not found user"));
        ChatRoom chatRoom = chatRoomRepository.findById(payload.getRoomId())
                .orElseThrow(()-> new NotFoundChatRoomException("not found chat room"));
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom,user,true)
                .orElseThrow(()-> new NotFoundChatParticipantException("not found chat participant"));
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .chatParticipant(chatParticipant)
                .build();
        chatMessageRepository.save(chatMessage);
        List<ChatParticipant> existChatParticipant = chatParticipantRepository.findAllByChatRoomAndStatus(chatRoom,true);
        List<ChatParticipant> unExistChatParticipant = chatParticipantRepository.findAllByChatRoomAndStatus(chatRoom,false);
        List<ReadStatus> existReadStatus = existChatParticipant.stream()
                .map(participant ->
                     ReadStatus.builder()
                            .status(true)
                            .chatMessage(chatMessage)
                            .chatParticipant(participant)
                            .build()
                ).toList();
        List<ReadStatus> unExistReadStatus = unExistChatParticipant.stream()
                .map(participant ->
                        ReadStatus.builder()
                                .status(false)
                                .chatMessage(chatMessage)
                                .chatParticipant(participant)
                                .build()
                ).toList();
        readStatusRepository.saveAll(existReadStatus);
        readStatusRepository.saveAll(unExistReadStatus);
    }


    private void emailCertification(EmailCertificationRequest emailCertificationRequest) throws MessagingException {
        emailProvider.sendCertificationMail(emailCertificationRequest.getEmail(), certificationNumber());
    }

    private String certificationNumber(){
        int number = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(number);
    }
}
