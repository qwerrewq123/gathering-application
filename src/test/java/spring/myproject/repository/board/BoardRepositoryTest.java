package spring.myproject.repository.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.entity.board.Board;
import spring.myproject.dto.response.board.querydto.BoardQuery;
import spring.myproject.dto.response.board.querydto.BoardsQuery;
import spring.myproject.entity.category.Category;
import spring.myproject.entity.enrollment.Enrollment;
import spring.myproject.repository.category.CategoryRepository;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.repository.enrollment.EnrollmentRepository;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.utils.DummyData.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.yml")
class BoardRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    GatheringRepository gatheringRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    EnrollmentRepository enrollmentRepository;
    Category category;
    Image userImage;
    Image gatheringImage;
    List<Image> boardImages;
    List<User> users;
    Gathering gathering;
    List<Enrollment> enrollments;
    List<Board> boards;
    @BeforeEach
    void beforeEach(){
        category = returnDummyCategory(1);
        userImage = returnDummyImage(1);
        users = List.of(returnDummyUser(1, userImage),
                returnDummyUser(2, userImage),
                returnDummyUser(3, userImage));
        User user1 = users.get(0);
        User user2 = users.get(1);
        User user3 = users.get(2);
        gatheringImage = returnDummyImage(1);
        gathering = returnDummyGathering(1, category, user1, gatheringImage);
        enrollments = List.of(returnDummyEnrollment(user1,gathering),
                returnDummyEnrollment(user2,gathering),
                returnDummyEnrollment(user3,gathering));
        boards = new ArrayList<>();
        boardImages = new ArrayList<>();
        for(int i= 0;i<3;i++){
            Board board = returnDummyBoard(users.get(i),gathering,i);
            boards.add(board);
            for(int j=1;j<=3;j++){
                Image boardImage = returnDummyImage(j);
                boardImage.changeBoard(board);
                boardImages.add(boardImage);
            }
        }
    }
    @Test
    void fetchBoard() {
        Board board = boards.get(0);
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(users);
        gatheringRepository.saveAll(List.of(gathering));
        enrollmentRepository.saveAll(enrollments);
        boardRepository.saveAll(boards);
        imageRepository.saveAll(boardImages);

        List<BoardQuery> boardQueries = boardRepository.fetchBoard(board.getId());

        assertThat(boardQueries).hasSize(3);
        assertThat(boardQueries).extracting("imageUrl")
                .containsExactly(
                        "image1Url","image2Url","image3Url"
                );
    }

    @Test
    void fetchBoards() {

        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage));
        userRepository.saveAll(users);
        gatheringRepository.saveAll(List.of(gathering));
        enrollmentRepository.saveAll(enrollments);
        boardRepository.saveAll(boards);
        imageRepository.saveAll(boardImages);

        Page<BoardsQuery> page = boardRepository.fetchBoards(PageRequest.of(0, 2));

        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
    }
}