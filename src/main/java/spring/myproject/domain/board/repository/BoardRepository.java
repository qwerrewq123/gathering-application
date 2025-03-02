package spring.myproject.domain.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.board.Board;
import spring.myproject.domain.board.dto.response.BoardsQuery;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("select b from Board  b join fetch Image i on i.board.id = b.id " +
            "where b.id = :boardId")
    Optional<Board> fetchBoard(Long boardId);

    @Query("select new spring.myproject.domain.board.dto.response." +
            "BoardsQuery(b.title,b.description,u.username,b.content,b.registerDate) " +
            "from Board b " +
            "join b.user u")
    Page<BoardsQuery> fetchBoards(Pageable pageable);
}
