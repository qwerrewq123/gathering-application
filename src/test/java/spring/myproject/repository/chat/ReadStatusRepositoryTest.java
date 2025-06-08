package spring.myproject.repository.chat;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import spring.myproject.entity.category.Category;
import spring.myproject.entity.enrollment.Enrollment;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.repository.category.CategoryRepository;
import spring.myproject.repository.enrollment.EnrollmentRepository;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.repository.user.UserRepository;


import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.entity.chat.ChatMessage;
import spring.myproject.entity.chat.ChatParticipant;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.entity.chat.ReadStatus;
import spring.myproject.entity.image.Image;
import spring.myproject.entity.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.utils.DummyData.*;
import static spring.myproject.utils.DummyData.returnDummyReadStatus;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.yml")
class ReadStatusRepositoryTest {
        @Autowired
        UserRepository userRepository;
        @Autowired
        ImageRepository imageRepository;
        @Autowired
        CategoryRepository categoryRepository;
        @Autowired
        GatheringRepository gatheringRepository;
        @Autowired
        EnrollmentRepository enrollmentRepository;
        @Autowired
        ChatMessageRepository chatMessageRepository;
        @Autowired
        ChatParticipantRepository chatParticipantRepository;
        @Autowired
        ChatRoomRepository chatRoomRepository;
        @Autowired
        ReadStatusRepository readStatusRepository;
        @Autowired
        EntityManager em;
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
            category = returnDummyCategory(1);
            gathering = returnDummyGathering(1, category, users.get(0), image);
            for(int i=0;i<5;i++){
                enrollments.add(returnDummyEnrollment(users.get(i),gathering));
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
        void readChatMessage() {
            ChatParticipant chatParticipant = chatParticipants.get(0);
            imageRepository.save(image);
            userRepository.saveAll(users);
            categoryRepository.save(category);
            gatheringRepository.save(gathering);
            chatRoomRepository.saveAll(chatRooms);
            chatParticipantRepository.saveAll(chatParticipants);
            chatMessageRepository.saveAll(chatMessages);
            readStatusRepository.saveAll(readStatuses);
            List<Long> chatMessagesIds = chatMessages.stream()
                    .map(ChatMessage::getId)
                    .collect(Collectors.toList());
            readStatusRepository.readChatMessage(chatParticipant.getId(),chatMessagesIds);
            em.flush();
            em.clear();

            Optional<ReadStatus> optionalReadStatus = readStatusRepository.findById(readStatuses.get(0).getId());

            assertThat(optionalReadStatus).isPresent()
                            .get().extracting("status").isEqualTo(true);
        }
}