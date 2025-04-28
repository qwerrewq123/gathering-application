package spring.myproject.repository.chat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.common.exception.chat.NotFoundChatRoomException;
import spring.myproject.dto.response.chat.query.AbleChatRoomElement;
import spring.myproject.dto.response.chat.query.MyChatRoomElement;
import spring.myproject.dto.response.chat.query.ParticipateChatRoomElement;
import spring.myproject.entity.category.Category;
import spring.myproject.entity.chat.ChatMessage;
import spring.myproject.entity.chat.ChatParticipant;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.dto.response.chat.query.ChatRoomElement;
import spring.myproject.entity.chat.ReadStatus;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.category.CategoryRepository;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;

import java.util.ArrayList;
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
    CategoryRepository categoryRepository;
    @Autowired
    GatheringRepository gatheringRepository;
    @Autowired
    ChatMessageRepository chatMessageRepository;
    @Autowired
    ChatParticipantRepository chatParticipantRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;
    @Autowired
    ReadStatusRepository readStatusRepository;
    @Test
    void fetchMyChatRooms() {
        Image image = returnDummyImage(1);
        User user1 = returnDummyUser(1, image);
        User user2 = returnDummyUser(2, image);
        User user3 = returnDummyUser(3, image);
        User user4 = returnDummyUser(4, image);
        User user5 = returnDummyUser(5, image);
        Category category = returnDummyCategory(1);
        Gathering gathering = returnDummyGathering(1, category, user1, image);
        ChatRoom chatRoom1 = returnDummyChatRoom(user1, gathering,1);
        ChatRoom chatRoom2 = returnDummyChatRoom(user1, gathering,2);
        ChatRoom chatRoom3 = returnDummyChatRoom(user1, gathering,3);
        ChatParticipant chatParticipant1 = returnDummyChatParticipant(user1, chatRoom1);
        ChatParticipant chatParticipant2 = returnDummyChatParticipant(user2, chatRoom1);
        ChatParticipant chatParticipant3 = returnDummyChatParticipant(user3, chatRoom1);
        ChatParticipant chatParticipant4 = returnDummyChatParticipant(user4, chatRoom1);
        ChatParticipant chatParticipant5 = returnDummyChatParticipant(user5, chatRoom1);
        ChatParticipant chatParticipant6 = returnDummyChatParticipant(user1, chatRoom2);
        ChatParticipant chatParticipant7 = returnDummyChatParticipant(user2, chatRoom2);
        ChatParticipant chatParticipant8 = returnDummyChatParticipant(user3, chatRoom2);
        ChatParticipant chatParticipant9 = returnDummyChatParticipant(user4, chatRoom2);
        ChatParticipant chatParticipant10 = returnDummyChatParticipant(user5, chatRoom2);
        ChatParticipant chatParticipant11 = returnDummyChatParticipant(user1, chatRoom3);
        ChatParticipant chatParticipant12 = returnDummyChatParticipant(user2, chatRoom3);
        ChatParticipant chatParticipant13 = returnDummyChatParticipant(user3, chatRoom3);
        ChatParticipant chatParticipant14 = returnDummyChatParticipant(user4, chatRoom3);
        ChatParticipant chatParticipant15 = returnDummyChatParticipant(user5, chatRoom3);
        List<ChatMessage> chatMessages = new ArrayList<>();
        List<ReadStatus> readStatuses = new ArrayList<>();
        for(int i = 1;i<=5;i++){
            ChatMessage chatMessage1 = returnDummyChatMessage(chatRoom1, chatParticipant1, i);
            ChatMessage chatMessage2 = returnDummyChatMessage(chatRoom2, chatParticipant1, i);
            ChatMessage chatMessage3 = returnDummyChatMessage(chatRoom3, chatParticipant1, i);
            chatMessages.add(chatMessage1);
            chatMessages.add(chatMessage2);
            chatMessages.add(chatMessage3);
            readStatuses.add(returnDummyReadStatus(chatParticipant1,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant2,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant3,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant4,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant5,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant1,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant2,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant3,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant4,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant5,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant1,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant2,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant3,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant4,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant5,chatMessage3));
        }
        imageRepository.save(image);
        userRepository.saveAll(List.of(user1,user2,user3,user4,user5));
        categoryRepository.save(category);
        gatheringRepository.save(gathering);
        chatRoomRepository.saveAll(List.of(chatRoom1,chatRoom2,chatRoom3));
        chatParticipantRepository.saveAll(List.of(chatParticipant1,chatParticipant2,chatParticipant3,chatParticipant4,chatParticipant5));
        chatParticipantRepository.saveAll(List.of(chatParticipant6,chatParticipant7,chatParticipant8,chatParticipant9,chatParticipant10));
        chatParticipantRepository.saveAll(List.of(chatParticipant11,chatParticipant12,chatParticipant13,chatParticipant14,chatParticipant15));
        chatMessageRepository.saveAll(chatMessages);
        readStatusRepository.saveAll(readStatuses);
        Page<MyChatRoomElement> page = chatRoomRepository.fetchMyChatRooms(PageRequest.of(0, 2), user1.getId());
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
    }

    @Test
    void fetchChatRooms() {
        Image image = returnDummyImage(1);
        User user1 = returnDummyUser(1, image);
        User user2 = returnDummyUser(2, image);
        User user3 = returnDummyUser(3, image);
        User user4 = returnDummyUser(4, image);
        User user5 = returnDummyUser(5, image);
        Category category = returnDummyCategory(1);
        Gathering gathering = returnDummyGathering(1, category, user1, image);
        ChatRoom chatRoom1 = returnDummyChatRoom(user1, gathering,1);
        ChatRoom chatRoom2 = returnDummyChatRoom(user1, gathering,2);
        ChatRoom chatRoom3 = returnDummyChatRoom(user1, gathering,3);
        ChatParticipant chatParticipant1 = returnDummyChatParticipant(user1, chatRoom1);
        ChatParticipant chatParticipant2 = returnDummyChatParticipant(user2, chatRoom1);
        ChatParticipant chatParticipant3 = returnDummyChatParticipant(user3, chatRoom1);
        ChatParticipant chatParticipant4 = returnDummyChatParticipant(user4, chatRoom1);
        ChatParticipant chatParticipant5 = returnDummyChatParticipant(user5, chatRoom1);
        ChatParticipant chatParticipant6 = returnDummyChatParticipant(user1, chatRoom2);
        ChatParticipant chatParticipant7 = returnDummyChatParticipant(user2, chatRoom2);
        ChatParticipant chatParticipant8 = returnDummyChatParticipant(user3, chatRoom2);
        ChatParticipant chatParticipant9 = returnDummyChatParticipant(user4, chatRoom2);
        ChatParticipant chatParticipant10 = returnDummyChatParticipant(user5, chatRoom2);
        ChatParticipant chatParticipant11 = returnDummyChatParticipant(user1, chatRoom3);
        ChatParticipant chatParticipant12 = returnDummyChatParticipant(user2, chatRoom3);
        ChatParticipant chatParticipant13 = returnDummyChatParticipant(user3, chatRoom3);
        ChatParticipant chatParticipant14 = returnDummyChatParticipant(user4, chatRoom3);
        ChatParticipant chatParticipant15 = returnDummyChatParticipant(user5, chatRoom3);
        List<ChatMessage> chatMessages = new ArrayList<>();
        List<ReadStatus> readStatuses = new ArrayList<>();
        for(int i = 1;i<=5;i++){
            ChatMessage chatMessage1 = returnDummyChatMessage(chatRoom1, chatParticipant1, i);
            ChatMessage chatMessage2 = returnDummyChatMessage(chatRoom2, chatParticipant1, i);
            ChatMessage chatMessage3 = returnDummyChatMessage(chatRoom3, chatParticipant1, i);
            chatMessages.add(chatMessage1);
            chatMessages.add(chatMessage2);
            chatMessages.add(chatMessage3);
            readStatuses.add(returnDummyReadStatus(chatParticipant1,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant2,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant3,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant4,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant5,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant1,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant2,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant3,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant4,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant5,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant1,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant2,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant3,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant4,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant5,chatMessage3));
        }
        imageRepository.save(image);
        userRepository.saveAll(List.of(user1,user2,user3,user4,user5));
        categoryRepository.save(category);
        gatheringRepository.save(gathering);
        chatRoomRepository.saveAll(List.of(chatRoom1,chatRoom2,chatRoom3));
        chatParticipantRepository.saveAll(List.of(chatParticipant1,chatParticipant2,chatParticipant3,chatParticipant4,chatParticipant5));
        chatParticipantRepository.saveAll(List.of(chatParticipant6,chatParticipant7,chatParticipant8,chatParticipant9,chatParticipant10));
        chatParticipantRepository.saveAll(List.of(chatParticipant11,chatParticipant12,chatParticipant13,chatParticipant14,chatParticipant15));
        chatMessageRepository.saveAll(chatMessages);
        readStatusRepository.saveAll(readStatuses);

        Page<ChatRoomElement> page = chatRoomRepository.fetchChatRooms(PageRequest.of(0, 2), user1.getId(),gathering.getId());
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
    }
    @Test
    void fetchAbleChatRooms(){
        Image image = returnDummyImage(1);
        User user1 = returnDummyUser(1, image);
        User user2 = returnDummyUser(2, image);
        User user3 = returnDummyUser(3, image);
        User user4 = returnDummyUser(3, image);
        User user5 = returnDummyUser(3, image);
        Category category = returnDummyCategory(1);
        Gathering gathering = returnDummyGathering(1, category, user1, image);
        ChatRoom chatRoom1 = returnDummyChatRoom(user1, gathering,1);
        ChatRoom chatRoom2 = returnDummyChatRoom(user1, gathering,2);
        ChatRoom chatRoom3 = returnDummyChatRoom(user1, gathering,3);
        ChatRoom chatRoom4 = returnDummyChatRoom(user1, gathering,4);
        ChatRoom chatRoom5 = returnDummyChatRoom(user1, gathering,5);
        ChatParticipant chatParticipant1 = returnDummyChatParticipant(user1, chatRoom1);
        ChatParticipant chatParticipant2 = returnDummyChatParticipant(user2, chatRoom1);
        ChatParticipant chatParticipant3 = returnDummyChatParticipant(user3, chatRoom1);
        ChatParticipant chatParticipant4 = returnDummyChatParticipant(user4, chatRoom1);
        ChatParticipant chatParticipant5 = returnDummyChatParticipant(user5, chatRoom1);
        ChatParticipant chatParticipant6 = returnDummyChatParticipant(user1, chatRoom2);
        ChatParticipant chatParticipant7 = returnDummyChatParticipant(user2, chatRoom2);
        ChatParticipant chatParticipant8 = returnDummyChatParticipant(user3, chatRoom2);
        ChatParticipant chatParticipant9 = returnDummyChatParticipant(user4, chatRoom2);
        ChatParticipant chatParticipant10 = returnDummyChatParticipant(user5, chatRoom2);
        ChatParticipant chatParticipant11 = returnDummyChatParticipant(user1, chatRoom3);
        ChatParticipant chatParticipant12 = returnDummyChatParticipant(user2, chatRoom3);
        ChatParticipant chatParticipant13 = returnDummyChatParticipant(user3, chatRoom3);
        ChatParticipant chatParticipant14 = returnDummyChatParticipant(user4, chatRoom3);
        ChatParticipant chatParticipant15 = returnDummyChatParticipant(user5, chatRoom3);
        List<ChatMessage> chatMessages = new ArrayList<>();
        List<ReadStatus> readStatuses = new ArrayList<>();
        for(int i = 1;i<=5;i++){
            ChatMessage chatMessage1 = returnDummyChatMessage(chatRoom1, chatParticipant1, i);
            ChatMessage chatMessage2 = returnDummyChatMessage(chatRoom2, chatParticipant1, i);
            ChatMessage chatMessage3 = returnDummyChatMessage(chatRoom3, chatParticipant1, i);
            chatMessages.add(chatMessage1);
            chatMessages.add(chatMessage2);
            chatMessages.add(chatMessage3);
            readStatuses.add(returnDummyReadStatus(chatParticipant1,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant2,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant3,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant4,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant5,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant1,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant2,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant3,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant4,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant5,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant1,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant2,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant3,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant4,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant5,chatMessage3));
        }
        imageRepository.save(image);
        userRepository.saveAll(List.of(user1,user2,user3,user4,user5));
        categoryRepository.save(category);
        gatheringRepository.save(gathering);
        chatRoomRepository.saveAll(List.of(chatRoom1,chatRoom2,chatRoom3,chatRoom4,chatRoom5));
        chatParticipantRepository.saveAll(List.of(chatParticipant1,chatParticipant2,chatParticipant3,chatParticipant4,chatParticipant5));
        chatParticipantRepository.saveAll(List.of(chatParticipant6,chatParticipant7,chatParticipant8,chatParticipant9,chatParticipant10));
        chatParticipantRepository.saveAll(List.of(chatParticipant11,chatParticipant12,chatParticipant13,chatParticipant14,chatParticipant15));
        chatMessageRepository.saveAll(chatMessages);
        readStatusRepository.saveAll(readStatuses);
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<AbleChatRoomElement> page = chatRoomRepository.fetchAbleChatRooms(pageRequest, user1.getId(), gathering.getId());
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(2);
    }
    @Test
    void fetchParticipantChatRooms(){
        Image image = returnDummyImage(1);
        User user1 = returnDummyUser(1, image);
        User user2 = returnDummyUser(2, image);
        User user3 = returnDummyUser(3, image);
        User user4 = returnDummyUser(3, image);
        User user5 = returnDummyUser(3, image);
        Category category = returnDummyCategory(1);
        Gathering gathering = returnDummyGathering(1, category, user1, image);
        ChatRoom chatRoom1 = returnDummyChatRoom(user1, gathering,1);
        ChatRoom chatRoom2 = returnDummyChatRoom(user1, gathering,2);
        ChatRoom chatRoom3 = returnDummyChatRoom(user1, gathering,3);
        ChatRoom chatRoom4 = returnDummyChatRoom(user1, gathering,4);
        ChatRoom chatRoom5 = returnDummyChatRoom(user1, gathering,5);
        ChatParticipant chatParticipant1 = returnDummyChatParticipant(user1, chatRoom1);
        ChatParticipant chatParticipant2 = returnDummyChatParticipant(user2, chatRoom1);
        ChatParticipant chatParticipant3 = returnDummyChatParticipant(user3, chatRoom1);
        ChatParticipant chatParticipant4 = returnDummyChatParticipant(user4, chatRoom1);
        ChatParticipant chatParticipant5 = returnDummyChatParticipant(user5, chatRoom1);
        ChatParticipant chatParticipant6 = returnDummyChatParticipant(user1, chatRoom2);
        ChatParticipant chatParticipant7 = returnDummyChatParticipant(user2, chatRoom2);
        ChatParticipant chatParticipant8 = returnDummyChatParticipant(user3, chatRoom2);
        ChatParticipant chatParticipant9 = returnDummyChatParticipant(user4, chatRoom2);
        ChatParticipant chatParticipant10 = returnDummyChatParticipant(user5, chatRoom2);
        ChatParticipant chatParticipant11 = returnDummyChatParticipant(user1, chatRoom3);
        ChatParticipant chatParticipant12 = returnDummyChatParticipant(user2, chatRoom3);
        ChatParticipant chatParticipant13 = returnDummyChatParticipant(user3, chatRoom3);
        ChatParticipant chatParticipant14 = returnDummyChatParticipant(user4, chatRoom3);
        ChatParticipant chatParticipant15 = returnDummyChatParticipant(user5, chatRoom3);
        List<ChatMessage> chatMessages = new ArrayList<>();
        List<ReadStatus> readStatuses = new ArrayList<>();
        for(int i = 1;i<=5;i++){
            ChatMessage chatMessage1 = returnDummyChatMessage(chatRoom1, chatParticipant1, i);
            ChatMessage chatMessage2 = returnDummyChatMessage(chatRoom2, chatParticipant1, i);
            ChatMessage chatMessage3 = returnDummyChatMessage(chatRoom3, chatParticipant1, i);
            chatMessages.add(chatMessage1);
            chatMessages.add(chatMessage2);
            chatMessages.add(chatMessage3);
            readStatuses.add(returnDummyReadStatus(chatParticipant1,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant2,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant3,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant4,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant5,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant1,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant2,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant3,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant4,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant5,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant1,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant2,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant3,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant4,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant5,chatMessage3));
        }
        imageRepository.save(image);
        userRepository.saveAll(List.of(user1,user2,user3,user4,user5));
        categoryRepository.save(category);
        gatheringRepository.save(gathering);
        chatRoomRepository.saveAll(List.of(chatRoom1,chatRoom2,chatRoom3,chatRoom4,chatRoom5));
        chatParticipantRepository.saveAll(List.of(chatParticipant1,chatParticipant2,chatParticipant3,chatParticipant4,chatParticipant5));
        chatParticipantRepository.saveAll(List.of(chatParticipant6,chatParticipant7,chatParticipant8,chatParticipant9,chatParticipant10));
        chatParticipantRepository.saveAll(List.of(chatParticipant11,chatParticipant12,chatParticipant13,chatParticipant14,chatParticipant15));
        chatMessageRepository.saveAll(chatMessages);
        readStatusRepository.saveAll(readStatuses);
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<ParticipateChatRoomElement> page = chatRoomRepository.fetchParticipateChatRooms(pageRequest, user1.getId(), gathering.getId());
        assertThat(page.getTotalPages()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(3);

    }
    @Test
    void fetchChatRoomById() {
        Image image = returnDummyImage(1);
        User user1 = returnDummyUser(1, image);
        User user2 = returnDummyUser(2, image);
        User user3 = returnDummyUser(3, image);
        User user4 = returnDummyUser(3, image);
        User user5 = returnDummyUser(3, image);
        Category category = returnDummyCategory(1);
        Gathering gathering = returnDummyGathering(1, category, user1, image);
        ChatRoom chatRoom1 = returnDummyChatRoom(user1, gathering,1);
        ChatRoom chatRoom2 = returnDummyChatRoom(user1, gathering,2);
        ChatRoom chatRoom3 = returnDummyChatRoom(user1, gathering,3);
        ChatRoom chatRoom4 = returnDummyChatRoom(user1, gathering,4);
        ChatRoom chatRoom5 = returnDummyChatRoom(user1, gathering,5);
        ChatParticipant chatParticipant1 = returnDummyChatParticipant(user1, chatRoom1);
        ChatParticipant chatParticipant2 = returnDummyChatParticipant(user2, chatRoom1);
        ChatParticipant chatParticipant3 = returnDummyChatParticipant(user3, chatRoom1);
        ChatParticipant chatParticipant4 = returnDummyChatParticipant(user4, chatRoom1);
        ChatParticipant chatParticipant5 = returnDummyChatParticipant(user5, chatRoom1);
        ChatParticipant chatParticipant6 = returnDummyChatParticipant(user1, chatRoom2);
        ChatParticipant chatParticipant7 = returnDummyChatParticipant(user2, chatRoom2);
        ChatParticipant chatParticipant8 = returnDummyChatParticipant(user3, chatRoom2);
        ChatParticipant chatParticipant9 = returnDummyChatParticipant(user4, chatRoom2);
        ChatParticipant chatParticipant10 = returnDummyChatParticipant(user5, chatRoom2);
        ChatParticipant chatParticipant11 = returnDummyChatParticipant(user1, chatRoom3);
        ChatParticipant chatParticipant12 = returnDummyChatParticipant(user2, chatRoom3);
        ChatParticipant chatParticipant13 = returnDummyChatParticipant(user3, chatRoom3);
        ChatParticipant chatParticipant14 = returnDummyChatParticipant(user4, chatRoom3);
        ChatParticipant chatParticipant15 = returnDummyChatParticipant(user5, chatRoom3);
        List<ChatMessage> chatMessages = new ArrayList<>();
        List<ReadStatus> readStatuses = new ArrayList<>();
        for(int i = 1;i<=5;i++){
            ChatMessage chatMessage1 = returnDummyChatMessage(chatRoom1, chatParticipant1, i);
            ChatMessage chatMessage2 = returnDummyChatMessage(chatRoom2, chatParticipant1, i);
            ChatMessage chatMessage3 = returnDummyChatMessage(chatRoom3, chatParticipant1, i);
            chatMessages.add(chatMessage1);
            chatMessages.add(chatMessage2);
            chatMessages.add(chatMessage3);
            readStatuses.add(returnDummyReadStatus(chatParticipant1,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant2,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant3,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant4,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant5,chatMessage1));
            readStatuses.add(returnDummyReadStatus(chatParticipant1,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant2,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant3,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant4,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant5,chatMessage2));
            readStatuses.add(returnDummyReadStatus(chatParticipant1,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant2,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant3,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant4,chatMessage3));
            readStatuses.add(returnDummyReadStatus(chatParticipant5,chatMessage3));
        }
        imageRepository.save(image);
        userRepository.saveAll(List.of(user1,user2,user3,user4,user5));
        categoryRepository.save(category);
        gatheringRepository.save(gathering);
        chatRoomRepository.saveAll(List.of(chatRoom1,chatRoom2,chatRoom3,chatRoom4,chatRoom5));
        chatParticipantRepository.saveAll(List.of(chatParticipant1,chatParticipant2,chatParticipant3,chatParticipant4,chatParticipant5));
        chatParticipantRepository.saveAll(List.of(chatParticipant6,chatParticipant7,chatParticipant8,chatParticipant9,chatParticipant10));
        chatParticipantRepository.saveAll(List.of(chatParticipant11,chatParticipant12,chatParticipant13,chatParticipant14,chatParticipant15));
        chatMessageRepository.saveAll(chatMessages);
        readStatusRepository.saveAll(readStatuses);
        ChatRoom chatRoom = chatRoomRepository.fetchChatRoomById(chatRoom1.getId())
                .orElseThrow(() -> new NotFoundChatRoomException("Not Found ChatRoom"));
        assertThat(chatRoom).isNotNull();
    }
}