package spring.myproject.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.domain.board.dto.request.AddBoardRequest;
import spring.myproject.domain.board.dto.response.AddBoardResponse;
import spring.myproject.domain.board.dto.response.BoardResponse;
import spring.myproject.domain.board.dto.response.BoardsResponse;
import spring.myproject.domain.board.service.BoardService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/gathering/{gatheringId}/board/{boardId}")
    public ResponseEntity<Object> fetchBoard(@PathVariable Long boardId,
                                             @PathVariable Long gatheringId,
                                             @AuthenticationPrincipal String username){
        BoardResponse fetchBoardResponse = boardService.fetchBoard(gatheringId,boardId,username);
        return ResponseEntity.ok(fetchBoardResponse);
    }
    @PostMapping("/gathering/{gatheringId}/board/")
    public ResponseEntity<Object> addBoard(@AuthenticationPrincipal String username,
                                           @PathVariable Long gatheringId,
                                           @RequestPart AddBoardRequest addBoardRequest,
                                           @RequestPart("file") List<MultipartFile> files) throws IOException {
        AddBoardResponse addBoardResponse = boardService.addBoard(username,addBoardRequest,files,gatheringId);
        return ResponseEntity.ok(addBoardResponse);
    }
    @GetMapping("/gathering/{gatheringId}/boards")
    public ResponseEntity<Object> fetchBoards(@AuthenticationPrincipal String username,
                                              @PathVariable Long gatheringId,
                                              @RequestParam String title,
                                              Integer pageNum, Integer pageSize){
        BoardsResponse fetchBoardsResponse = boardService.fetchBoards(gatheringId,username,pageNum,pageSize);
        return ResponseEntity.ok(fetchBoardsResponse);

    }

}
