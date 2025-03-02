package spring.myproject.domain.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.board.Board;
import spring.myproject.domain.board.dto.response.QueryBoardDto;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("select b from Board  b where b.id = :boardId")
    Optional<Board> fetchBoard(Long boardId);

    @Query("select new spring.myproject.domain.board.dto.response." +
            "QueryBoardDto(b.title,b.description,b.image.url,u.username,i.url,b.registerDate) " +
            "from Board b join b.user u left join u.profileImage i")
    Page<QueryBoardDto> fetchBoards(Pageable pageable);
}
