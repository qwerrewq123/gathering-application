package spring.myproject.repository.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.Board;
import spring.myproject.dto.response.board.BoardQuery;
import spring.myproject.dto.response.board.BoardsQuery;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("select new spring.myproject.domain.board.dto.response.BoardQuery" +
            "(b.title,b.description,i.url,u.username,im.url,b.registerDate) " +
            "from Board b left join b.user u left join u.profileImage im " +
            "left join Image i on i.board.id = b.id where b.id = :boardId")
    List<BoardQuery> fetchBoard(Long boardId);

    @Query("select new spring.myproject.domain.board.dto.response." +
            "BoardsQuery(b.title,b.description,u.username,b.content,b.registerDate) " +
            "from Board b " +
            "join b.user u")
    Page<BoardsQuery> fetchBoards(Pageable pageable);
}
