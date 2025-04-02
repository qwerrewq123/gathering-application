package spring.myproject.repository.chat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.entity.chat.ChatMessage;
import spring.myproject.entity.chat.ChatParticipant;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.entity.chat.ReadStatus;
import spring.myproject.dto.response.chat.query.ChatMessageElement;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.utils.DummyData.*;
import static spring.myproject.utils.DummyData.returnDummyChatParticipant;

@SpringBootTest
@Transactional
class ChatMessageRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ChatMessageRepository chatMessageRepository;
    @Autowired
    ChatParticipantRepository chatParticipantRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;
    @Autowired
    ReadStatusRepository readStatusRepository;
    @Test
    void findByChatRoomAndChatParticipant() {
        Image image = returnDummyImage(1);
        User user1 = returnDummyUser(1, image);
        User user2 = returnDummyUser(2, image);
        User user3 = returnDummyUser(3, image);
        User user4 = returnDummyUser(3, image);
        User user5 = returnDummyUser(3, image);
        ChatRoom chatRoom = returnDummyChatRoom(user1, 1);
        ChatParticipant chatParticipant1 = returnDummyChatParticipant(user1, chatRoom);
        ChatParticipant chatParticipant2 = returnDummyChatParticipant(user2, chatRoom);
        ChatParticipant chatParticipant3 = returnDummyChatParticipant(user3, chatRoom);
        ChatParticipant chatParticipant4 = returnDummyChatParticipant(user4, chatRoom);
        ChatParticipant chatParticipant5 = returnDummyChatParticipant(user5, chatRoom);
        List<ChatMessage> chatMessages = new ArrayList<>();
        List<ReadStatus> readStatuses = new ArrayList<>();
        for(int i = 1;i<=5;i++){
            ChatMessage chatMessage = returnDummyChatMessage(chatRoom, chatParticipant1, i);
            chatMessages.add(chatMessage);
            readStatuses.add(returnDummyReadStatus(chatParticipant1,chatMessage));
            readStatuses.add(returnDummyReadStatus(chatParticipant2,chatMessage));
            readStatuses.add(returnDummyReadStatus(chatParticipant3,chatMessage));
            readStatuses.add(returnDummyReadStatus(chatParticipant4,chatMessage));
            readStatuses.add(returnDummyReadStatus(chatParticipant5,chatMessage));
        }
        imageRepository.save(image);
        userRepository.saveAll(List.of(user1,user2,user3,user4,user5));
        chatRoomRepository.saveAll(List.of(chatRoom));
        chatParticipantRepository.saveAll(List.of(chatParticipant1,chatParticipant2,chatParticipant3,chatParticipant4,chatParticipant5));
        chatMessageRepository.saveAll(chatMessages);
        readStatusRepository.saveAll(readStatuses);

        List<ChatMessage> fetchChatMessages = chatMessageRepository.findByChatRoomAndChatParticipant(chatRoom, chatParticipant1);
        assertThat(fetchChatMessages).hasSize(5);
        assertThat(fetchChatMessages).extracting("content")
                .containsExactly(
                        "content1","content2","content3","content4","content5"
                );
    }

    @Test
    void fetchMessages() {
        Image image = returnDummyImage(1);
        User user1 = returnDummyUser(1, image);
        User user2 = returnDummyUser(2, image);
        User user3 = returnDummyUser(3, image);
        User user4 = returnDummyUser(3, image);
        User user5 = returnDummyUser(3, image);
        ChatRoom chatRoom = returnDummyChatRoom(user1, 1);
        ChatParticipant chatParticipant1 = returnDummyChatParticipant(user1, chatRoom);
        ChatParticipant chatParticipant2 = returnDummyChatParticipant(user2, chatRoom);
        ChatParticipant chatParticipant3 = returnDummyChatParticipant(user3, chatRoom);
        ChatParticipant chatParticipant4 = returnDummyChatParticipant(user4, chatRoom);
        ChatParticipant chatParticipant5 = returnDummyChatParticipant(user5, chatRoom);
        List<ChatMessage> chatMessages = new ArrayList<>();
        List<ReadStatus> readStatuses = new ArrayList<>();
        for(int i = 1;i<=5;i++){
            ChatMessage chatMessage = returnDummyChatMessage(chatRoom, chatParticipant1, i);
            chatMessages.add(chatMessage);
            readStatuses.add(returnDummyReadStatus(chatParticipant1,chatMessage));
            readStatuses.add(returnDummyReadStatus(chatParticipant2,chatMessage));
            readStatuses.add(returnDummyReadStatus(chatParticipant3,chatMessage));
            readStatuses.add(returnDummyReadStatus(chatParticipant4,chatMessage));
            readStatuses.add(returnDummyReadStatus(chatParticipant5,chatMessage));
        }
        imageRepository.save(image);
        userRepository.saveAll(List.of(user1,user2,user3,user4,user5));
        chatRoomRepository.saveAll(List.of(chatRoom));
        chatParticipantRepository.saveAll(List.of(chatParticipant1,chatParticipant2,chatParticipant3,chatParticipant4,chatParticipant5));
        chatMessageRepository.saveAll(chatMessages);
        readStatusRepository.saveAll(readStatuses);

        List<ChatMessageElement> chatMessageRespons = chatMessageRepository.fetchMessages(chatRoom.getId(), chatParticipant1.getId());
        assertThat(chatMessageRespons.size()).isEqualTo(5);
        assertThat(chatMessageRespons).extracting("content")
                .containsExactly(
                        "content1","content2","content3","content4","content5"
                );
    }
}