package spring.myproject.service.chat;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import spring.myproject.dto.response.chat.query.ChatMessageElement;
import spring.myproject.dto.response.chat.query.ChatRoomElement;
import spring.myproject.dto.response.chat.query.MyChatRoomElement;
import spring.myproject.entity.chat.ChatMessage;
import spring.myproject.entity.chat.ChatParticipant;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.common.exception.chat.NotFoundChatParticipantException;
import spring.myproject.common.exception.chat.NotFoundChatRoomException;
import spring.myproject.repository.chat.ChatMessageRepository;
import spring.myproject.repository.chat.ChatParticipantRepository;
import spring.myproject.repository.chat.ChatRoomRepository;
import spring.myproject.repository.chat.ReadStatusRepository;
import spring.myproject.entity.user.User;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static spring.myproject.dto.response.chat.ChatResponseDto.*;
import static spring.myproject.utils.ConstClass.SUCCESS_CODE;
import static spring.myproject.utils.ConstClass.SUCCESS_MESSAGE;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;


    public AddChatRoomResponse addChatRoom(String roomName, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
        ChatRoom chatRoom = ChatRoom.of(roomName, user);
        ChatParticipant chatParticipant = ChatParticipant.of(chatRoom, user,false);
        chatRoomRepository.save(chatRoom);
        chatParticipantRepository.save(chatParticipant);
        return AddChatRoomResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE, chatRoom.getId());
    }

    public LeaveChatResponse leaveChat(Long chatId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new NotFoundUserException("no exist User!!"));
        ChatRoom chatRoom = chatRoomRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundChatRoomException("no exist ChatRoom!!"));
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom,user,true)
                .orElseThrow(()-> new NotFoundChatParticipantException("no exist ChatParticipant!!"));
        chatParticipant.changeStatus(false);
        return LeaveChatResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public AttendChatResponse attendChat(Long roomId, String username) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundChatRoomException("no exist ChatRoom!!"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new NotFoundUserException("no exist User!!"));
        Optional<ChatParticipant> optionalChatParticipant = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom,user,false);

        if(optionalChatParticipant.isPresent()) optionalChatParticipant.get().changeStatus(true);
        if(optionalChatParticipant.isEmpty()) chatParticipantRepository.save(ChatParticipant.of(chatRoom,user,true));
        chatRoom.changeCount(chatRoom.getCount()+1);
        return AttendChatResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public ChatMessagesResponse fetchMessages(Long roomId, String username) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundChatRoomException("no exist ChatRoom!!"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new NotFoundUserException("no exist User!!"));
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom,user,true)
                .orElseThrow(()->new NotFoundChatParticipantException("no exist ChatParticipant!!"));
        Long chatParticipantId = chatParticipant.getId();
        List<ChatMessageElement> content = chatMessageRepository.fetchMessages(roomId,chatParticipantId);
        return ChatMessagesResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE, content);
    }

    public ReadChatMessageResponse readChatMessage(Long chatId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new NotFoundUserException("no exist User!!"));
        ChatRoom chatRoom = chatRoomRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundChatRoomException("no exist ChatRoom!!"));
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom,user,true)
                .orElseThrow(()-> new NotFoundChatParticipantException("no exist ChatParticipant!!"));
        List<ChatMessage> chatMessages = chatMessageRepository.findChatMessageByChatRoom(chatRoom);
        Long chatParticipantId = chatParticipant.getId();
        List<Long> chatMessagesId = chatMessages.stream().map(c -> c.getId()).toList();
        readStatusRepository.readChatMessage(chatParticipantId,chatMessagesId);
        return ReadChatMessageResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public ChatRoomResponse fetchChatRooms(Integer pageNum, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
        Long userId = user.getId();
        PageRequest pageRequest = PageRequest.of(pageNum, 5);
        Page<ChatRoomElement> page = chatRoomRepository.fetchChatRooms(pageRequest,userId);
        List<ChatRoomElement> content = page.getContent();
        boolean hasNext = page.hasNext();
        return ChatRoomResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,content,hasNext);
    }

    public MyChatRoomResponse fetchMyChatRooms(Integer pageNum, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
        Long userId = user.getId();
        PageRequest pageRequest = PageRequest.of(pageNum, 5);
        Page<MyChatRoomElement> page = chatRoomRepository.fetchMyChatRooms(pageRequest,userId);
        List<MyChatRoomElement> content = page.getContent();
        boolean hasNext = page.hasNext();
        return MyChatRoomResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,content,hasNext);
    }


    public boolean isRoomParticipant(String username, long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundChatRoomException("no exist ChatRoom!!"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new NotFoundUserException("no exist User!!"));
        chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom,user,true)
                .orElseThrow(()->new NotFoundChatParticipantException("no exist ChatParticipant!!"));
        return true;
    }

}
