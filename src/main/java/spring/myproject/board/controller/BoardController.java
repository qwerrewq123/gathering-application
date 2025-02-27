package spring.myproject.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.board.dto.request.AddBoardRequest;
import spring.myproject.board.dto.response.AddBoardResponse;
import spring.myproject.board.dto.response.FetchBoardResponse;
import spring.myproject.board.dto.response.FetchBoardsResponse;
import spring.myproject.board.service.BoardService;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board/{boardId}")
    public ResponseEntity<Object> fetchBoard(@PathVariable Long boardId,
                                             @AuthenticationPrincipal String username){
        FetchBoardResponse fetchBoardResponse = boardService.fetchBoard(boardId,username);
        return ResponseEntity.ok(fetchBoardResponse);
    }
    @PostMapping("/board/meeting/{meetingId}")
    public ResponseEntity<Object> addBoard(@AuthenticationPrincipal String username,
                                           @RequestPart AddBoardRequest addBoardRequest,
                                           @RequestPart("file") MultipartFile file,
                                           Long meetingId){
        AddBoardResponse addBoardResponse = boardService.addBoard(username,addBoardRequest,file,meetingId);
        return ResponseEntity.ok(addBoardResponse);
    }
    @GetMapping("/boards")
    public ResponseEntity<Object> fetchBoards(@AuthenticationPrincipal String username,
                                              @RequestParam String title,
                                              Integer pageNum){
        FetchBoardsResponse fetchBoardsResponse = boardService.fetchBoards(username,pageNum);
        return ResponseEntity.ok(fetchBoardsResponse);

    }

}
