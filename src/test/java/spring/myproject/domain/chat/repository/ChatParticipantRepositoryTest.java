package spring.myproject.domain.chat.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.myproject.domain.chat.ChatParticipant;
import spring.myproject.domain.chat.ChatRoom;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.util.DummyData.*;
import static spring.myproject.util.DummyData.returnDummyChatParticipant;

@SpringBootTest
class ChatParticipantRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ChatParticipantRepository chatParticipantRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Test
    void findByChatRoomAndUserAndStatus() {
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

        Optional<ChatParticipant> optionalChatParticipant = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom1, user2, true);
        assertThat(optionalChatParticipant).isPresent();
        assertThat(optionalChatParticipant.get()).extracting("user").isEqualTo(user2);
    }

    @Test
    void findAllByChatRoomAndStatus() {
        Image image = returnDummyImage(1);
        User user1 = returnDummyUser(1, image);
        User user2 = returnDummyUser(2, image);
        User user3 = returnDummyUser(3, image);
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
        ChatParticipant chatParticipant4 = returnDummyChatParticipant(user3, chatRoom1);
        ChatParticipant chatParticipant5 = returnDummyChatParticipant(user3, chatRoom2);
        ChatParticipant chatParticipant6 = returnDummyChatParticipant(user3, chatRoom3);

        imageRepository.save(image);
        userRepository.saveAll(List.of(user1,user2,user3));
        chatRoomRepository.saveAll(List.of(chatRoom1,chatRoom2,chatRoom3,chatRoom4,chatRoom5));
        chatParticipantRepository.saveAll(List.of(createChatParticipant1,createChatParticipant2,createChatParticipant3,createChatParticipant4,createChatParticipant5));
        chatParticipantRepository.saveAll(List.of(chatParticipant1,chatParticipant2,chatParticipant3,chatParticipant4,chatParticipant5,chatParticipant6));

        List<ChatParticipant> chatParticipants = chatParticipantRepository.findAllByChatRoomAndStatus(chatRoom1, true);
        assertThat(chatParticipants).hasSize(2);
        assertThat(chatParticipants).extracting("user")
                .containsExactly(
                        tuple(user1),
                        tuple(user2),
                        tuple(user3)
                );
    }
}