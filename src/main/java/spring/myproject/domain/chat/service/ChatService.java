package spring.myproject.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import spring.myproject.domain.chat.ChatMessage;
import spring.myproject.domain.chat.ChatParticipant;
import spring.myproject.domain.chat.ChatRoom;
import spring.myproject.domain.chat.dto.response.*;
import spring.myproject.domain.chat.exception.NotFoundChatMessageException;
import spring.myproject.domain.chat.exception.NotFoundChatParticipantException;
import spring.myproject.domain.chat.exception.NotFoundChatRoomException;
import spring.myproject.domain.chat.repository.ChatMessageRepository;
import spring.myproject.domain.chat.repository.ChatParticipantRepository;
import spring.myproject.domain.chat.repository.ChatRoomRepository;
import spring.myproject.domain.chat.repository.ReadStatusRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.exception.NotFoundUserException;
import spring.myproject.domain.user.repository.UserRepository;

import static spring.myproject.util.ConstClass.SUCCESS_CODE;
import static spring.myproject.util.ConstClass.SUCCESS_MESSAGE;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;

    public AddChatRoomResponse addChatRoom(String roomName, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
        ChatRoom chatRoom = ChatRoom.builder()
                .name(roomName)
                .createdBy(user)
                .count(1)
                .build();
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .user(user)
                .build();
        chatRoomRepository.save(chatRoom);
        chatParticipantRepository.save(chatParticipant);
        return AddChatRoomResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public ChatRoomsResponse fetchChatRooms(Integer pageNum,String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
        PageRequest pageRequest = PageRequest.of(pageNum, 5);
        Page<ChatRoomResponse> page = chatRoomRepository.fetchChatRooms(pageRequest);
        return ChatRoomsResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .page(page)
                .build();
    }

    public ChatRoomsResponse fetchMyChatRooms(Integer pageNum,String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
        Long userId = user.getId();
        PageRequest pageRequest = PageRequest.of(pageNum, 5);
        Page<ChatRoomResponse> page = chatRoomRepository.fetchMyChatRooms(pageRequest,userId);
        return ChatRoomsResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .page(page)
                .build();
    }

    public LeaveChatResponse leaveChat(Long chatId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new NotFoundUserException("no exist User!!"));
        ChatRoom chatRoom = chatRoomRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundChatRoomException("no exist ChatRoom!!"));
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomAndUser(chatRoom,user)
                .orElseThrow(()-> new NotFoundChatParticipantException("no exist ChatParticipant!!"));

        chatParticipantRepository.delete(chatParticipant);
        chatRoom.changeCount();
        if(chatRoom.getCount() == 0){
            chatRoomRepository.delete(chatRoom);
        }

        return LeaveChatResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .build();
    }

    public ReadChatMessageResponse readChatMessage(Long chatId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new NotFoundUserException("no exist User!!"));
        ChatRoom chatRoom = chatRoomRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundChatRoomException("no exist ChatRoom!!"));
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomAndUser(chatRoom,user)
                .orElseThrow(()-> new NotFoundChatParticipantException("no exist ChatParticipant!!"));
        ChatMessage chatMessage = chatMessageRepository.findByChatRoomAndChatParticipant(chatRoom,chatParticipant)
                .orElseThrow(()-> new NotFoundChatMessageException("no exist ChatMessage!!"));
        Long chatParticipantId = chatParticipant.getId();
        Long chatMessageId = chatMessage.getId();
        readStatusRepository.readChatMessage(chatParticipantId,chatMessageId);
        return ReadChatMessageResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .build();
    }
}
