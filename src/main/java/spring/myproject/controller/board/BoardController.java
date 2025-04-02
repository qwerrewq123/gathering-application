package spring.myproject.controller.board;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.common.Username;
import spring.myproject.dto.response.board.BoardResponseDto;
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
                                             @Username String username){
        BoardResponse fetchBoardResponse = boardService.fetchBoard(gatheringId,boardId,username);
        return ResponseEntity.ok(fetchBoardResponse);
    }
    @PostMapping("/gathering/{gatheringId}/board")
    public ResponseEntity<Object> addBoard(@Username String username,
                                           @PathVariable Long gatheringId,
                                           @RequestPart AddBoardRequest addBoardRequest,
                                           @RequestPart("files") List<MultipartFile> files) throws IOException {
        AddBoardResponse addBoardResponse = boardService.addBoard(username,addBoardRequest,files,gatheringId);
        return ResponseEntity.ok(addBoardResponse);
    }
    @GetMapping("/gathering/{gatheringId}/boards")
    public ResponseEntity<Object> fetchBoards(@Username String username,
                                              @PathVariable Long gatheringId,
                                              @RequestParam(defaultValue = "") String title,
                                              Integer pageNum, Integer pageSize){
        BoardsResponse fetchBoardsResponse = boardService.fetchBoards(gatheringId,username,pageNum,pageSize);
        return ResponseEntity.ok(fetchBoardsResponse);

    }

}
