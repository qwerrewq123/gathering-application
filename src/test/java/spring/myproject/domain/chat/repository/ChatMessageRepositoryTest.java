package spring.myproject.domain.chat.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.myproject.domain.chat.ChatMessage;
import spring.myproject.domain.chat.ChatParticipant;
import spring.myproject.domain.chat.ChatRoom;
import spring.myproject.domain.chat.ReadStatus;
import spring.myproject.domain.chat.dto.response.ChatMessageResponse;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static spring.myproject.util.DummyData.*;
import static spring.myproject.util.DummyData.returnDummyChatParticipant;

@SpringBootTest
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
    //TODO : 로직수정 후 테스트작성
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
        userRepository.saveAll(List.of(user1,user2,user3));
        chatRoomRepository.saveAll(List.of(chatRoom));
        chatParticipantRepository.saveAll(List.of(chatParticipant1,chatParticipant2,chatParticipant3,chatParticipant4,chatParticipant5));
        chatMessageRepository.saveAll(chatMessages);
        readStatusRepository.saveAll(readStatuses);
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
        userRepository.saveAll(List.of(user1,user2,user3));
        chatRoomRepository.saveAll(List.of(chatRoom));
        chatParticipantRepository.saveAll(List.of(chatParticipant1,chatParticipant2,chatParticipant3,chatParticipant4,chatParticipant5));
        chatMessageRepository.saveAll(chatMessages);
        readStatusRepository.saveAll(readStatuses);

        List<ChatMessageResponse> chatMessageResponses = chatMessageRepository.fetchMessages(chatRoom.getId(), chatParticipant1.getId());
        assertThat(chatMessageResponses.size()).isEqualTo(5);
        assertThat(chatMessageResponses).extracting("content")
                .containsExactly(
                        tuple("content1"),
                        tuple("content2"),
                        tuple("content3"),
                        tuple("content4"),
                        tuple("content5")
                );
    }
}