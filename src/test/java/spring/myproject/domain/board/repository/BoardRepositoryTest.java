package spring.myproject.domain.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import spring.myproject.domain.Board;
import spring.myproject.dto.response.board.BoardQuery;
import spring.myproject.dto.response.board.BoardsQuery;
import spring.myproject.domain.Category;
import spring.myproject.repository.category.CategoryRepository;
import spring.myproject.domain.Gathering;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.domain.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.domain.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.repository.board.BoardRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static spring.myproject.util.DummyData.*;

@SpringBootTest
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
    @Test
    void fetchBoard() {
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        Image boardImage1 = returnDummyImage(1);
        Image boardImage2 = returnDummyImage(2);
        Image boardImage3 = returnDummyImage(3);
        User user1 = returnDummyUser(1, userImage);
        User user2 = returnDummyUser(2, userImage);
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage);
        Board board = returnDummyBoard(user2, gathering, 1);
        boardImage1.changeBoard(board);
        boardImage2.changeBoard(board);
        boardImage3.changeBoard(board);
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage,boardImage1,boardImage2,boardImage3));
        userRepository.saveAll(List.of(user1,user2));
        gatheringRepository.saveAll(List.of(gathering));
        boardRepository.saveAll(List.of(board));

        List<BoardQuery> boardQueries = boardRepository.fetchBoard(board.getId());
        assertThat(boardQueries).hasSize(3);
        assertThat(boardQueries).extracting("imageUrl")
                .containsExactly(
                        tuple("image1Url"),
                        tuple("image2Url"),
                        tuple("image3Url")
                );
    }

    @Test
    void fetchBoards() {
        Category category = returnDummyCategory(1);
        Image userImage = returnDummyImage(1);
        Image gatheringImage = returnDummyImage(1);
        Image boardImage1 = returnDummyImage(1);
        Image boardImage2 = returnDummyImage(2);
        Image boardImage3 = returnDummyImage(3);
        Image boardImage4 = returnDummyImage(4);
        Image boardImage5 = returnDummyImage(5);
        Image boardImage6 = returnDummyImage(6);
        Image boardImage7 = returnDummyImage(7);
        Image boardImage8 = returnDummyImage(8);
        Image boardImage9 = returnDummyImage(9);
        User user1 = returnDummyUser(1, userImage);
        User user2 = returnDummyUser(2, userImage);
        User user3 = returnDummyUser(3, userImage);
        Gathering gathering = returnDummyGathering(1, category, user1, gatheringImage);
        Board board1 = returnDummyBoard(user1, gathering, 1);
        Board board2 = returnDummyBoard(user2, gathering, 2);
        Board board3 = returnDummyBoard(user3, gathering, 3);
        boardImage1.changeBoard(board1);
        boardImage2.changeBoard(board1);
        boardImage3.changeBoard(board1);
        boardImage4.changeBoard(board2);
        boardImage5.changeBoard(board2);
        boardImage6.changeBoard(board2);
        boardImage7.changeBoard(board3);
        boardImage8.changeBoard(board3);
        boardImage9.changeBoard(board3);
        categoryRepository.save(category);
        imageRepository.saveAll(List.of(userImage,gatheringImage,boardImage1,boardImage2,boardImage3,boardImage4,boardImage5,boardImage6,boardImage7,boardImage8,boardImage9));
        userRepository.saveAll(List.of(user1,user2,user3));
        gatheringRepository.saveAll(List.of(gathering));
        boardRepository.saveAll(List.of(board1,board2,board3));

        Page<BoardsQuery> page = boardRepository.fetchBoards(PageRequest.of(0, 2));
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
    }
}