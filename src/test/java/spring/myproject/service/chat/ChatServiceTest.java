package spring.myproject.service.chat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.dto.request.chat.ChatRequestDto;
import spring.myproject.entity.chat.ChatParticipant;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.user.Role;
import spring.myproject.entity.user.User;
import spring.myproject.common.exception.chat.NotFoundChatParticipantException;
import spring.myproject.common.exception.chat.NotFoundChatRoomException;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.repository.chat.ChatMessageRepository;
import spring.myproject.repository.chat.ChatParticipantRepository;
import spring.myproject.repository.chat.ChatRoomRepository;
import spring.myproject.repository.chat.ReadStatusRepository;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.repository.user.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static spring.myproject.dto.request.chat.ChatRequestDto.*;
import static spring.myproject.dto.response.chat.ChatResponseDto.*;
import static spring.myproject.utils.ConstClass.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {
    @InjectMocks
    ChatService chatService;
    @Mock
    UserRepository userRepository;
    @Mock
    GatheringRepository gatheringRepository;
    @Mock
    ChatRoomRepository chatRoomRepository;
    @Mock
    ChatParticipantRepository chatParticipantRepository;
    @Test
    void addChatRoom() {
        User mockUser = new User(1L,"true username","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        Gathering mockGathering = new Gathering(1L,null,null,null,null,mockUser,0,null,null,null);
        AddChatRequest addChatRequest = AddChatRequest.builder()
                .title("title")
                .description("description")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        when(gatheringRepository.findById(1L)).thenReturn(Optional.of(mockGathering));
        when(gatheringRepository.findById(2L)).thenReturn(Optional.empty());
        when(chatRoomRepository.save(any())).thenReturn(null);
        when(chatParticipantRepository.save(any())).thenReturn(null);
        assertThatThrownBy(()-> chatService.addChatRoom(2L,addChatRequest,2L))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()-> chatService.addChatRoom(2L,addChatRequest,1L))
                .isInstanceOf(NotFoundGatheringException.class);
        AddChatRoomResponse addChatRoomResponse = chatService.addChatRoom(1L,addChatRequest,1L);
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
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser2));
        when(userRepository.findById(3L)).thenReturn(Optional.empty());
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(mock(ChatRoom.class)));
        when(chatRoomRepository.findById(2L)).thenReturn(Optional.empty());
        when(chatParticipantRepository.findByChatRoomAndUserAndStatus(any(ChatRoom.class),eq(mockUser1),eq(true)))
                .thenReturn(Optional.of(mock(ChatParticipant.class)));
        when(chatParticipantRepository.findByChatRoomAndUserAndStatus(any(ChatRoom.class),eq(mockUser2),eq(true)))
                .thenReturn(Optional.empty());
        assertThatThrownBy(()-> chatService.leaveChat(2L,3L))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()-> chatService.leaveChat(2L,2L))
                .isInstanceOf(NotFoundChatRoomException.class);
        assertThatThrownBy(()-> chatService.leaveChat(1L,2L))
                .isInstanceOf(NotFoundChatParticipantException.class);
        LeaveChatResponse leaveChatResponse = chatService.leaveChat(1L, 1L);
        assertThat(leaveChatResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE,SUCCESS_MESSAGE);
    }


}