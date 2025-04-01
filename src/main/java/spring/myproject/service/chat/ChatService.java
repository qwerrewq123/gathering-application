package spring.myproject.service.chat;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import spring.myproject.entity.chat.ChatMessage;
import spring.myproject.entity.chat.ChatParticipant;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.entity.chat.ReadStatus;
import spring.myproject.dto.response.chat.*;
import spring.myproject.exception.chat.NotFoundChatMessageException;
import spring.myproject.exception.chat.NotFoundChatParticipantException;
import spring.myproject.exception.chat.NotFoundChatRoomException;
import spring.myproject.repository.chat.ChatMessageRepository;
import spring.myproject.repository.chat.ChatParticipantRepository;
import spring.myproject.repository.chat.ChatRoomRepository;
import spring.myproject.repository.chat.ReadStatusRepository;
import spring.myproject.entity.user.User;
import spring.myproject.exception.user.NotFoundUserException;
import spring.myproject.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

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
        ChatParticipant chatParticipant = ChatParticipant.of(chatRoom, user);
        chatRoomRepository.save(chatRoom);
        chatParticipantRepository.save(chatParticipant);
        return AddChatRoomResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public ChatRoomsResponse fetchChatRooms(Integer pageNum,String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
        Long userId = user.getId();
        PageRequest pageRequest = PageRequest.of(pageNum, 5);
        Page<ChatRoomResponse> page = chatRoomRepository.fetchChatRooms(pageRequest,userId);
        return ChatRoomsResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,page);
    }

    public ChatMyRoomsResponse fetchMyChatRooms(Integer pageNum, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
        Long userId = user.getId();
        PageRequest pageRequest = PageRequest.of(pageNum, 5);
        Page<ChatMyRoomResponse> page = chatRoomRepository.fetchMyChatRooms(pageRequest,userId);
        return ChatMyRoomsResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,page);
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

    public ReadChatMessageResponse readChatMessage(Long chatId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new NotFoundUserException("no exist User!!"));
        ChatRoom chatRoom = chatRoomRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundChatRoomException("no exist ChatRoom!!"));
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom,user,true)
                .orElseThrow(()-> new NotFoundChatParticipantException("no exist ChatParticipant!!"));
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomAndChatParticipant(chatRoom,chatParticipant);
        if(chatMessages.isEmpty()) throw new NotFoundChatMessageException("no exist ChatMessage!!!");
        Long chatParticipantId = chatParticipant.getId();
        List<Long> chatMessagesId = chatMessages.stream().map(c -> c.getId()).toList();
        readStatusRepository.readChatMessage(chatParticipantId,chatMessagesId);
        return ReadChatMessageResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }



    public void saveMessage(Long roomId,ChatMessageRequest chatMessageRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundChatRoomException("no exist ChatRoom!!"));
        User user = userRepository.findByUsername(chatMessageRequest.getUsername())
                .orElseThrow(()->new NotFoundUserException("no exist User!!"));
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom,user,true)
                .orElseThrow(()->new NotFoundChatParticipantException("no exist ChatParticipant!!"));
        ChatMessage chatMessage = ChatMessage.of(chatRoom,chatParticipant,chatMessageRequest);
        List<ChatParticipant> trueChatParticipants =
                chatParticipantRepository.findAllByChatRoomAndStatus(chatRoom, true);
        List<ChatParticipant> falseChatParticipants =
                chatParticipantRepository.findAllByChatRoomAndStatus(chatRoom, false);
        chatMessageRepository.save(chatMessage);
        saveReadStatus(trueChatParticipants, chatMessage, falseChatParticipants);
    }

    public AttendChatResponse attendChat(Long roomId, String username) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundChatRoomException("no exist ChatRoom!!"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new NotFoundUserException("no exist User!!"));
        Optional<ChatParticipant> optionalChatParticipant = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom,user,false);

        if(optionalChatParticipant.isPresent()) optionalChatParticipant.get().changeStatus(true);
        if(optionalChatParticipant.isEmpty()){
            chatParticipantRepository.save(ChatParticipant.builder()
                    .chatRoom(chatRoom)
                    .user(user)
                    .status(true)
                    .build());
        }
        chatRoom.changeCount(chatRoom.getCount()+1);
        return AttendChatResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public FetchMessagesResponse fetchMessages(Long roomId, String username) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundChatRoomException("no exist ChatRoom!!"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new NotFoundUserException("no exist User!!"));
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom,user,true)
                .orElseThrow(()->new NotFoundChatParticipantException("no exist ChatParticipant!!"));
        Long chatParticipantId = chatParticipant.getId();
        List<ChatMessageResponse> chatMessageResponses = chatMessageRepository.fetchMessages(roomId,chatParticipantId);
        return FetchMessagesResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,chatMessageResponses);
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

    private void saveReadStatus(List<ChatParticipant> trueChatParticipants, ChatMessage chatMessage, List<ChatParticipant> falseChatParticipants) {
        for (ChatParticipant trueChatParticipant : trueChatParticipants) {
            readStatusRepository.save(ReadStatus.builder()
                    .chatMessage(chatMessage)
                    .chatParticipant(trueChatParticipant)
                    .status(true)
                    .build());
        }
        for (ChatParticipant falsechatParticipant : falseChatParticipants) {
            readStatusRepository.save(ReadStatus.builder()
                    .chatMessage(chatMessage)
                    .chatParticipant(falsechatParticipant)
                    .status(false)
                    .build());
        }
    }
}
