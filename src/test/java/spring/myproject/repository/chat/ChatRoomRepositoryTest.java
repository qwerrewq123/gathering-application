package spring.myproject.repository.chat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.entity.chat.ChatParticipant;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.dto.response.chat.ChatMyRoomResponse;
import spring.myproject.dto.response.chat.ChatRoomResponse;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.utils.DummyData.*;

@SpringBootTest
@Transactional
class ChatRoomRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ChatParticipantRepository chatParticipantRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;
    @Test
    void fetchChatRooms() {
        Image image = returnDummyImage(1);
        User user1 = returnDummyUser(1, image);
        User user2 = returnDummyUser(2, image);
        ChatRoom chatRoom1 = returnDummyChatRoom(user1, 1);
        ChatRoom chatRoom2 = returnDummyChatRoom(user1, 2);
        ChatRoom chatRoom3 = returnDummyChatRoom(user1, 3);
        ChatRoom chatRoom4 = returnDummyChatRoom(user1, 4);
        ChatRoom chatRoom5 = returnDummyChatRoom(user1, 5);
        ChatParticipant createChatParticipant1 = returnDummyChatParticipant(user1, chatRoom1);
        ChatParticipant createChatParticipant2 = returnDummyChatParticipant(user1,chatRoom2);
        ChatParticipant createChatParticipant3 = returnDummyChatParticipant(user1, chatRoom3);
        ChatParticipant createChatParticipant4 = returnDummyChatParticipant(user1, chatRoom4);
        ChatParticipant createChatParticipant5 = returnDummyChatParticipant(user1, chatRoom5);
        ChatParticipant chatParticipant1 = returnDummyChatParticipant(user2, chatRoom1);
        ChatParticipant chatParticipant2 = returnDummyChatParticipant(user2, chatRoom2);
        ChatParticipant chatParticipant3 = returnDummyChatParticipant(user2, chatRoom3);

        imageRepository.save(image);
        userRepository.saveAll(List.of(user1,user2));
        chatRoomRepository.saveAll(List.of(chatRoom1,chatRoom2,chatRoom3,chatRoom4,chatRoom5));
        chatParticipantRepository.saveAll(List.of(createChatParticipant1,createChatParticipant2,createChatParticipant3,createChatParticipant4,createChatParticipant5));
        chatParticipantRepository.saveAll(List.of(chatParticipant1,chatParticipant2,chatParticipant3));
        //TODO : 쿼리수정
        Page<ChatRoomResponse> page = chatRoomRepository.fetchChatRooms(PageRequest.of(0, 2), user2.getId());
        assertThat(page.getTotalPages()).isEqualTo(4);
        assertThat(page.getTotalElements()).isEqualTo(8);
    }

    @Test
    void fetchMyChatRooms() {
        Image image = returnDummyImage(1);
        User user1 = returnDummyUser(1, image);
        User user2 = returnDummyUser(2, image);
        ChatRoom chatRoom1 = returnDummyChatRoom(user1, 1);
        ChatRoom chatRoom2 = returnDummyChatRoom(user1, 2);
        ChatRoom chatRoom3 = returnDummyChatRoom(user1, 3);
        ChatRoom chatRoom4 = returnDummyChatRoom(user1, 4);
        ChatRoom chatRoom5 = returnDummyChatRoom(user1, 5);

        ChatParticipant createChatParticipant1 = returnDummyChatParticipant(user1, chatRoom1);
        ChatParticipant createChatParticipant2 = returnDummyChatParticipant(user1,chatRoom2);
        ChatParticipant createChatParticipant3 = returnDummyChatParticipant(user1, chatRoom3);
        ChatParticipant createChatParticipant4 = returnDummyChatParticipant(user1, chatRoom4);
        ChatParticipant createChatParticipant5 = returnDummyChatParticipant(user1, chatRoom5);
        ChatParticipant chatParticipant1 = returnDummyChatParticipant(user2, chatRoom1);
        ChatParticipant chatParticipant2 = returnDummyChatParticipant(user2, chatRoom2);
        ChatParticipant chatParticipant3 = returnDummyChatParticipant(user2, chatRoom3);

        imageRepository.save(image);
        userRepository.saveAll(List.of(user1,user2));
        chatRoomRepository.saveAll(List.of(chatRoom1,chatRoom2,chatRoom3,chatRoom4,chatRoom5));
        chatParticipantRepository.saveAll(List.of(createChatParticipant1,createChatParticipant2,createChatParticipant3,createChatParticipant4,createChatParticipant5));
        chatParticipantRepository.saveAll(List.of(chatParticipant1,chatParticipant2,chatParticipant3));
        //TODO : 쿼리수정
        Page<ChatMyRoomResponse> page = chatRoomRepository.fetchMyChatRooms(PageRequest.of(0, 2), user2.getId());

        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
    }
}