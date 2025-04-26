package spring.myproject.repository.chat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.entity.category.Category;
import spring.myproject.entity.chat.ChatParticipant;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.utils.DummyData.*;
import static spring.myproject.utils.DummyData.returnDummyChatParticipant;

@SpringBootTest
@Transactional
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
        Category category = returnDummyCategory(1);
        Gathering gathering = returnDummyGathering(1, category, user1, image);
        ChatRoom chatRoom1 = returnDummyChatRoom(user1, gathering,1);
        ChatRoom chatRoom2 = returnDummyChatRoom(user1, gathering,2);
        ChatRoom chatRoom3 = returnDummyChatRoom(user1, gathering,3);
        ChatRoom chatRoom4 = returnDummyChatRoom(user1, gathering,4);
        ChatRoom chatRoom5 = returnDummyChatRoom(user1, gathering,5);
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
        User user3 = returnDummyUser(3, image);Category category = returnDummyCategory(1);
        Gathering gathering = returnDummyGathering(1, category, user1, image);
        ChatRoom chatRoom1 = returnDummyChatRoom(user1, gathering,1);
        ChatRoom chatRoom2 = returnDummyChatRoom(user1, gathering,2);
        ChatRoom chatRoom3 = returnDummyChatRoom(user1, gathering,3);
        ChatRoom chatRoom4 = returnDummyChatRoom(user1, gathering,4);
        ChatRoom chatRoom5 = returnDummyChatRoom(user1, gathering,5);
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
        assertThat(chatParticipants).hasSize(3);
        assertThat(chatParticipants).extracting("user")
                .containsExactly(
                        user1,user2,user3
                );
    }
}