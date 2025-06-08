package spring.myproject.service.chat;

import org.hibernate.annotations.DialectOverride;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.entity.chat.ChatParticipant;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.user.User;
import spring.myproject.common.exception.chat.NotFoundChatParticipantException;
import spring.myproject.common.exception.chat.NotFoundChatRoomException;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.repository.chat.ChatParticipantRepository;
import spring.myproject.repository.chat.ChatRoomRepository;
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
import static spring.myproject.utils.MockData.*;

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
    @DisplayName("Throws NotFoundUserException")
    @Test
    void addChatRoomThrowsNotFoundUserException(){
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());
        AddChatRequest addChatRequest = AddChatRequest.builder()
                .build();
        assertThatThrownBy(()->chatService.addChatRoom(1L,addChatRequest,1L))
                .isInstanceOf(NotFoundUserException.class);
    }
    @DisplayName("Throws NotFoundGatheringException")
    @Test
    void addChatRoomThrowsNotFoundGatheringException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(gatheringRepository.findById(1L))
                .thenReturn(Optional.empty());
        AddChatRequest addChatRequest = AddChatRequest.builder()
                .build();
        assertThatThrownBy(()->chatService.addChatRoom(1L,addChatRequest,1L))
                .isInstanceOf(NotFoundGatheringException.class);
    }
    @DisplayName("Return Normal Response")
    @Test
    void addChatRoom() {
        User mockUser = returnMockUser(1L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(gatheringRepository.findById(1L))
                .thenReturn(Optional.of(mockGathering));
        AddChatRequest addChatRequest = AddChatRequest.builder()
                .build();
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(gatheringRepository.findById(1L))
                .thenReturn(Optional.of(mockGathering));
        when(chatRoomRepository.save(any()))
                .thenReturn(mock(ChatRoom.class));
        when(chatParticipantRepository.save(any()))
                .thenReturn(mock(ChatParticipant.class));

        AddChatRoomResponse addChatRoomResponse = chatService.addChatRoom(1L,addChatRequest,1L);

        assertThat(addChatRoomResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
    @DisplayName("Throws NotFoundUserException")
    @Test
    void leaveChatThrowsNotFoundUserException(){
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->chatService.leaveChat(1L,1L))
                .isInstanceOf(NotFoundUserException.class);
    }
    @DisplayName("Throws NotFoundChatRoomException")
    @Test
    void leaveChatThrowsNotFoundChatRoomException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(chatRoomRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->chatService.leaveChat(1L,1L))
                .isInstanceOf(NotFoundChatRoomException.class);
    }
    @DisplayName("Throws NotFoundChatParticipantException")
    @Test
    void leaveChatThrowsNotFoundChatParticipantException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        ChatRoom mockChatRoom = returnMockChatRoom(mockUser);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(chatRoomRepository.findById(1L))
                .thenReturn(Optional.of(mockChatRoom));
        when(chatParticipantRepository.findByChatRoomAndUserAndStatus(any(ChatRoom.class),any(User.class),eq(true)))
                .thenReturn(Optional.empty());
        assertThatThrownBy(()->chatService.leaveChat(1L,1L))
                .isInstanceOf(NotFoundChatParticipantException.class);
    }
    @DisplayName("Return Normal Response")
    @Test
    void leaveChat() {
        User mockUser = returnMockUser(1L,"true username","true password");
        ChatRoom mockChatRoom = returnMockChatRoom(mockUser);
        ChatParticipant mockChatParticipant = returnMockChatParticipant(mockChatRoom,mockUser);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(chatRoomRepository.findById(1L))
                .thenReturn(Optional.of(mockChatRoom));
        when(chatParticipantRepository.findByChatRoomAndUserAndStatus(any(ChatRoom.class),any(User.class),eq(true)))
                .thenReturn(Optional.of(mockChatParticipant));

        LeaveChatResponse leaveChatResponse = chatService.leaveChat(1L, 1L);

        assertThat(leaveChatResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    @DisplayName("Throws NotFoundChatRoomException")
    @Test
    void attendChatThrowsNotFoundChatRoomException(){
        when(chatRoomRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->chatService.attendChat(1L,1L))
                .isInstanceOf(NotFoundChatRoomException.class);
    }

    @DisplayName("Throws NotFoundUserException")
    @Test
    void attendChatThrowsNotFoundUserException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        ChatRoom mockChatRoom = returnMockChatRoom(mockUser);
        when(chatRoomRepository.findById(1L))
                .thenReturn(Optional.of(mockChatRoom));
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());
        assertThatThrownBy(()->chatService.attendChat(1L,1L))
                .isInstanceOf(NotFoundUserException.class);
    }

    @DisplayName("Return Normal Response")
    @Test
    void attendChat(){
        User mockUser = returnMockUser(1L,"true username","true password");
        ChatRoom mockChatRoom = returnMockChatRoom(mockUser);
        ChatParticipant mockChatParticipant = returnMockChatParticipant(mockChatRoom,mockUser);
        when(chatRoomRepository.findById(1L))
                .thenReturn(Optional.of(mockChatRoom));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(chatParticipantRepository.findByChatRoomAndUserAndStatus(any(ChatRoom.class),any(User.class),eq(false)))
                .thenReturn(Optional.of(mockChatParticipant));

        AttendChatResponse attendChatResponse = chatService.attendChat(1L, 1L);

        assertThat(attendChatResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    @DisplayName("Throws NotFoundChatRoomException")
    @Test
    void isRoomParticipantThrowsNotFoundChatRoomException(){
        when(chatRoomRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->chatService.isRoomParticipant(1L,1L))
                .isInstanceOf(NotFoundChatRoomException.class);
    }

    @DisplayName("Throws NotFoundUserException")
    @Test
    void isRoomParticipantThrowsNotFoundUserException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        ChatRoom mockChatRoom = returnMockChatRoom(mockUser);
        when(chatRoomRepository.findById(1L))
                .thenReturn(Optional.of(mockChatRoom));
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->chatService.isRoomParticipant(1L,1L))
                .isInstanceOf(NotFoundUserException.class);
    }

    @DisplayName("Throws NotFoundChatParticipantException")
    @Test
    void isRoomParticipantThrowsNotFoundChatParticipantException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        ChatRoom mockChatRoom = returnMockChatRoom(mockUser);
        when(chatRoomRepository.findById(1L))
                .thenReturn(Optional.of(mockChatRoom));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(chatParticipantRepository.findByChatRoomAndUserAndStatus(any(ChatRoom.class),any(User.class),eq(true)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->chatService.isRoomParticipant(1L,1L))
                .isInstanceOf(NotFoundChatParticipantException.class);
    }

    @DisplayName("Return True")
    @Test
    void isRoomParticipant(){
        User mockUser = returnMockUser(1L,"true username","true password");
        ChatRoom mockChatRoom = returnMockChatRoom(mockUser);
        ChatParticipant mockChatParticipant = returnMockChatParticipant(mockChatRoom,mockUser);
        when(chatRoomRepository.findById(1L))
                .thenReturn(Optional.of(mockChatRoom));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(chatParticipantRepository.findByChatRoomAndUserAndStatus(any(ChatRoom.class),any(User.class),eq(true)))
                .thenReturn(Optional.of(mockChatParticipant));

        assertThat(chatService.isRoomParticipant(1L,1L))
                .isEqualTo(true);
    }



}