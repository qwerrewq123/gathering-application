package spring.myproject.controller.board;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.common.annotation.Id;
import spring.myproject.service.board.BoardService;

import java.io.IOException;
import java.util.List;

import static spring.myproject.dto.request.board.BoardRequestDto.*;
import static spring.myproject.dto.response.board.BoardResponseDto.*;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/gathering/{gatheringId}/board/{boardId}")
    public ResponseEntity<Object> fetchBoard(@PathVariable Long boardId,
                                             @PathVariable Long gatheringId,
                                             @Id Long userId){
        BoardResponse fetchBoardResponse = boardService.fetchBoard(gatheringId,boardId,userId);
        return ResponseEntity.ok(fetchBoardResponse);
    }
    @PostMapping("/gathering/{gatheringId}/board")
    public ResponseEntity<Object> addBoard(@Id Long userId,
                                           @PathVariable Long gatheringId,
                                           @RequestPart AddBoardRequest addBoardRequest,
                                           @RequestPart("files") List<MultipartFile> files) throws IOException {
        AddBoardResponse addBoardResponse = boardService.addBoard(userId,addBoardRequest,files,gatheringId);
        return ResponseEntity.ok(addBoardResponse);
    }
    @GetMapping("/gathering/{gatheringId}/boards")
    public ResponseEntity<BoardsResponse> fetchBoards(
                                              @PathVariable Long gatheringId,
                                              Integer pageNum, Integer pageSize){
        BoardsResponse fetchBoardsResponse = boardService.fetchBoards(gatheringId,pageNum,pageSize);
        return ResponseEntity.ok(fetchBoardsResponse);

    }

}
