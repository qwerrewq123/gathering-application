package spring.myproject.repository.chat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.common.exception.chat.NotFoundChatParticipantException;
import spring.myproject.dto.response.chat.query.ParticipantElement;
import spring.myproject.entity.category.Category;
import spring.myproject.entity.chat.ChatMessage;
import spring.myproject.entity.chat.ChatParticipant;
import spring.myproject.entity.chat.ChatRoom;
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
    void findByChatRoomAndUserAndStatus() {
        Image image = returnDummyImage(1);
        User user1 = returnDummyUser(1, image);
        User user2 = returnDummyUser(2, image);
        User user3 = returnDummyUser(3, image);
        User user4 = returnDummyUser(3, image);
        User user5 = returnDummyUser(3, image);
        Category category = returnDummyCategory(1);
        Gathering gathering = returnDummyGathering(1, category, user1, image);
        ChatRoom chatRoom = returnDummyChatRoom(user1, gathering,1);
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
        categoryRepository.save(category);
        gatheringRepository.save(gathering);
        chatRoomRepository.saveAll(List.of(chatRoom));
        chatParticipantRepository.saveAll(List.of(chatParticipant1,chatParticipant2,chatParticipant3,chatParticipant4,chatParticipant5));
        chatMessageRepository.saveAll(chatMessages);
        readStatusRepository.saveAll(readStatuses);

        ChatParticipant fetchChatParticipant1 = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom, user1, true)
                        .orElseThrow(()-> new NotFoundChatParticipantException("Not Found ChatParticipant!!"));
        assertThat(fetchChatParticipant1).isNotNull();
        assertThat(fetchChatParticipant1).extracting("user").isEqualTo(user1);
        ChatParticipant fetchChatParticipant2 = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom, user1, true)
                .orElseThrow(()-> new NotFoundChatParticipantException("Not Found ChatParticipant!!"));
        assertThat(fetchChatParticipant2).isNotNull();
        assertThat(fetchChatParticipant2).extracting("user").isEqualTo(user1);
        ChatParticipant fetchChatParticipant3= chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom, user1, true)
                .orElseThrow(()-> new NotFoundChatParticipantException("Not Found ChatParticipant!!"));
        assertThat(fetchChatParticipant3).isNotNull();
        assertThat(fetchChatParticipant3).extracting("user").isEqualTo(user1);
        ChatParticipant fetchChatParticipant4 = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom, user1, true)
                .orElseThrow(()-> new NotFoundChatParticipantException("Not Found ChatParticipant!!"));
        assertThat(fetchChatParticipant4).isNotNull();
        assertThat(fetchChatParticipant4).extracting("user").isEqualTo(user1);
        ChatParticipant fetchChatParticipant5 = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom, user1, true)
                .orElseThrow(()-> new NotFoundChatParticipantException("Not Found ChatParticipant!!"));
        assertThat(fetchChatParticipant5).isNotNull();
        assertThat(fetchChatParticipant5).extracting("user").isEqualTo(user1);
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
        categoryRepository.save(category);
        gatheringRepository.save(gathering);
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
    @Test
    void fetchParticipant(){
        Image image = returnDummyImage(1);
        User user1 = returnDummyUser(1, image);
        User user2 = returnDummyUser(2, image);
        User user3 = returnDummyUser(3, image);
        User user4 = returnDummyUser(3, image);
        User user5 = returnDummyUser(3, image);
        Category category = returnDummyCategory(1);
        Gathering gathering = returnDummyGathering(1, category, user1, image);
        ChatRoom chatRoom = returnDummyChatRoom(user1, gathering,1);
        ChatParticipant chatParticipant1 = returnDummyChatParticipant(user1, chatRoom);
        ChatParticipant chatParticipant2 = returnDummyChatParticipant(user2, chatRoom);
        ChatParticipant chatParticipant3 = returnDummyChatParticipant(user3, chatRoom);
        ChatParticipant chatParticipant4 = returnDummyChatParticipant(user4, chatRoom);
        ChatParticipant chatParticipant5 = returnDummyChatParticipant(user5, chatRoom);
        imageRepository.save(image);
        userRepository.saveAll(List.of(user1,user2,user3,user4,user5));
        categoryRepository.save(category);
        gatheringRepository.save(gathering);
        chatRoomRepository.saveAll(List.of(chatRoom));
        chatParticipantRepository.saveAll(List.of(chatParticipant1,chatParticipant2,chatParticipant3,chatParticipant4,chatParticipant5));
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<ParticipantElement> page = chatParticipantRepository.fetchParticipant(chatRoom.getId(), user1.getId(), pageRequest);
        assertThat(page.getTotalPages()).isEqualTo(3);
        assertThat(page.getContent().getFirst()).extracting("userId").isEqualTo(user1.getId());
    }


}