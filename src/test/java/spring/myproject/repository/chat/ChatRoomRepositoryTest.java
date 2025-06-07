package spring.myproject.repository.chat;

import org.junit.jupiter.api.BeforeEach;
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
import spring.myproject.entity.enrollment.Enrollment;
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
    Image image;
    List<User> users;
    List<Enrollment> enrollments;
    Category category;
    Gathering gathering;
    List<ChatRoom> chatRooms;
    List<ChatParticipant> chatParticipants;
    List<ChatMessage> chatMessages;
    List<ReadStatus> readStatuses;
    @BeforeEach
    void beforeEach(){
        image = returnDummyImage(1);
        users = new ArrayList<>();
        enrollments = new ArrayList<>();
        chatParticipants = new ArrayList<>();
        chatMessages = new ArrayList<>();
        readStatuses = new ArrayList<>();
        for(int i=1 ;i<=5;i++){
            users.add(returnDummyUser(i,image));
        }
        Category category = returnDummyCategory(1);
        Gathering gathering = returnDummyGathering(1, category, users.get(0), image);
        for(int i=0;i<5;i++){
            enrollments.add(returnDummyEnrollment(users.get(0),gathering));
        }
        chatRooms = List.of(returnDummyChatRoom(users.get(0), gathering,1),
                returnDummyChatRoom(users.get(0), gathering,2),
                returnDummyChatRoom(users.get(0), gathering,3));
        for(int i= 0;i<3;i++){
            for(int j= 0;j<5;j++){
                chatParticipants.add(returnDummyChatParticipant(users.get(j),chatRooms.get(i)));
            }
        }
        for(int i = 0;i<3;i++){
            for(int j= 0;j<5;j++){
                ChatMessage chatMessage = returnDummyChatMessage(chatRooms.get(i),chatParticipants.get(0),j+1);
                chatMessages.add(chatMessage);
                for(int k=0;k<5;k++){
                    readStatuses.add(returnDummyReadStatus(chatParticipants.get(k),chatMessage));
                }
            }
        }
    }
    @Test
    void fetchMyChatRooms() {
        User user = users.get(0);
        imageRepository.save(image);
        userRepository.saveAll(users);
        categoryRepository.save(category);
        gatheringRepository.save(gathering);
        chatRoomRepository.saveAll(chatRooms);
        chatParticipantRepository.saveAll(chatParticipants);
        chatMessageRepository.saveAll(chatMessages);
        readStatusRepository.saveAll(readStatuses);

        Page<MyChatRoomElement> page = chatRoomRepository.fetchMyChatRooms(PageRequest.of(0, 2), user.getId());

        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
    }

    @Test
    void fetchChatRooms() {
        User user = users.get(0);
        imageRepository.save(image);
        userRepository.saveAll(users);
        categoryRepository.save(category);
        gatheringRepository.save(gathering);
        chatRoomRepository.saveAll(chatRooms);
        chatParticipantRepository.saveAll(chatParticipants);
        chatMessageRepository.saveAll(chatMessages);
        readStatusRepository.saveAll(readStatuses);

        Page<ChatRoomElement> page = chatRoomRepository.fetchChatRooms(PageRequest.of(0, 2), user.getId(),gathering.getId());

        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
    }
    @Test
    void fetchAbleChatRooms(){
        User user = users.get(0);
        imageRepository.save(image);
        userRepository.saveAll(users);
        categoryRepository.save(category);
        gatheringRepository.save(gathering);
        chatRoomRepository.saveAll(chatRooms);
        chatParticipantRepository.saveAll(chatParticipants);
        chatMessageRepository.saveAll(chatMessages);
        readStatusRepository.saveAll(readStatuses);
        PageRequest pageRequest = PageRequest.of(0, 1);

        Page<AbleChatRoomElement> page = chatRoomRepository.fetchAbleChatRooms(pageRequest, user.getId(), gathering.getId());

        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(2);
    }
    @Test
    void fetchParticipantChatRooms(){
        User user = users.get(0);
        imageRepository.save(image);
        userRepository.saveAll(users);
        categoryRepository.save(category);
        gatheringRepository.save(gathering);
        chatRoomRepository.saveAll(chatRooms);
        chatParticipantRepository.saveAll(chatParticipants);
        chatMessageRepository.saveAll(chatMessages);
        readStatusRepository.saveAll(readStatuses);
        PageRequest pageRequest = PageRequest.of(0, 1);

        Page<ParticipateChatRoomElement> page = chatRoomRepository.fetchParticipateChatRooms(pageRequest, user.getId(), gathering.getId());

        assertThat(page.getTotalPages()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(3);

    }
    @Test
    void fetchChatRoomById() {
        ChatRoom chatRoom = chatRooms.get(0);
        imageRepository.save(image);
        userRepository.saveAll(users);
        categoryRepository.save(category);
        gatheringRepository.save(gathering);
        chatRoomRepository.saveAll(chatRooms);
        chatParticipantRepository.saveAll(chatParticipants);
        chatMessageRepository.saveAll(chatMessages);
        readStatusRepository.saveAll(readStatuses);

        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.fetchChatRoomById(chatRoom.getId());

        assertThat(optionalChatRoom).isPresent();
    }
}