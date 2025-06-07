package spring.myproject.repository.chat;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
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

@SpringBootTest
@Transactional
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
            users = List.of(returnDummyUser(1, image),
                    returnDummyUser(2, image),
                    returnDummyUser(3, image),
                    returnDummyUser(4, image),
                    returnDummyUser(5, image));
            Category category = returnDummyCategory(1);
            Gathering gathering = returnDummyGathering(1, category, users.get(0), image);
            enrollments = List.of(returnDummyEnrollment(users.get(0),gathering),
                    returnDummyEnrollment(users.get(1),gathering),
                    returnDummyEnrollment(users.get(2),gathering),
                    returnDummyEnrollment(users.get(3),gathering),
                    returnDummyEnrollment(users.get(4),gathering));
            chatRooms = List.of(returnDummyChatRoom(users.get(0), gathering,1));
            chatParticipants = List.of(returnDummyChatParticipant(users.get(0), chatRooms.get(0)),
                    returnDummyChatParticipant(users.get(1), chatRooms.get(0)),
                    returnDummyChatParticipant(users.get(2), chatRooms.get(0)),
                    returnDummyChatParticipant(users.get(3), chatRooms.get(0)),
                    returnDummyChatParticipant(users.get(4), chatRooms.get(0)));
            chatMessages = new ArrayList<>();
            readStatuses = new ArrayList<>();
            for(int i = 1;i<=5;i++){
                ChatMessage chatMessage = returnDummyChatMessage(chatRooms.get(0), chatParticipants.get(0), i);
                chatMessages.add(chatMessage);
                readStatuses.add(returnDummyReadStatus(chatParticipants.get(0),chatMessage));
                readStatuses.add(returnDummyReadStatus(chatParticipants.get(1),chatMessage));
                readStatuses.add(returnDummyReadStatus(chatParticipants.get(2),chatMessage));
                readStatuses.add(returnDummyReadStatus(chatParticipants.get(3),chatMessage));
                readStatuses.add(returnDummyReadStatus(chatParticipants.get(4),chatMessage));
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