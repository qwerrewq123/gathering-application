package spring.myproject.service.chat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import spring.myproject.dto.response.chat.AddChatRoomResponse;
import spring.myproject.dto.response.chat.LeaveChatResponse;
import spring.myproject.entity.chat.ChatParticipant;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.entity.user.Role;
import spring.myproject.entity.user.User;
import spring.myproject.exception.chat.NotFoundChatParticipantException;
import spring.myproject.exception.chat.NotFoundChatRoomException;
import spring.myproject.exception.user.NotFoundUserException;
import spring.myproject.repository.chat.ChatMessageRepository;
import spring.myproject.repository.chat.ChatParticipantRepository;
import spring.myproject.repository.chat.ChatRoomRepository;
import spring.myproject.repository.chat.ReadStatusRepository;
import spring.myproject.repository.user.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static spring.myproject.utils.ConstClass.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ChatServiceTest {
    @Autowired
    ChatService chatService;
    @MockitoBean
    ChatRoomRepository chatRoomRepository;
    @MockitoBean
    ChatParticipantRepository chatParticipantRepository;
    @MockitoBean
    ChatMessageRepository chatMessageRepository;
    @MockitoBean
    ReadStatusRepository readStatusRepository;
    @MockitoBean
    UserRepository userRepository;
    @Test
    void addChatRoom() {
        User mockUser = new User(1L,"true username","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        when(userRepository.findByUsername("true username")).thenReturn(Optional.of(mockUser));
        when(userRepository.findByUsername("false username")).thenReturn(Optional.empty());
        assertThatThrownBy(()-> chatService.addChatRoom("roomName","false username"))
                .isInstanceOf(NotFoundUserException.class);
        AddChatRoomResponse addChatRoomResponse = chatService.addChatRoom("roomName", "true username");
        assertThat(addChatRoomResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE,SUCCESS_MESSAGE);
    }


    @Test
    void leaveChat() {
        User mockUser1 = new User(1L,"true username1","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        User mockUser2 = new User(1L,"true username2","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        when(userRepository.findByUsername("true username1")).thenReturn(Optional.of(mockUser1));
        when(userRepository.findByUsername("true username2")).thenReturn(Optional.of(mockUser2));
        when(userRepository.findByUsername("false username")).thenReturn(Optional.empty());
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(mock(ChatRoom.class)));
        when(chatRoomRepository.findById(2L)).thenReturn(Optional.empty());
        when(chatParticipantRepository.findByChatRoomAndUserAndStatus(any(ChatRoom.class),eq(mockUser1),eq(true)))
                .thenReturn(Optional.of(mock(ChatParticipant.class)));
        when(chatParticipantRepository.findByChatRoomAndUserAndStatus(any(ChatRoom.class),eq(mockUser2),eq(true)))
                .thenReturn(Optional.empty());
        assertThatThrownBy(()-> chatService.leaveChat(2L,"false username"))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()-> chatService.leaveChat(2L,"true username2"))
                .isInstanceOf(NotFoundChatRoomException.class);
        assertThatThrownBy(()-> chatService.leaveChat(1L,"true username2"))
                .isInstanceOf(NotFoundChatParticipantException.class);
        LeaveChatResponse leaveChatResponse = chatService.leaveChat(1L, "true username1");
        assertThat(leaveChatResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE,SUCCESS_MESSAGE);
    }


}