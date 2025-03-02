package spring.myproject.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import spring.myproject.domain.chat.dto.response.AddChatRoomResponse;
import spring.myproject.domain.chat.dto.response.ChatRoomsResponse;
import spring.myproject.domain.chat.dto.response.LeaveChatResponse;
import spring.myproject.domain.chat.dto.response.ReadChatMessageResponse;
import spring.myproject.domain.chat.service.ChatService;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<AddChatRoomResponse> addChatRoom(@RequestParam String roomName, @AuthenticationPrincipal String username){
        AddChatRoomResponse addChatResponse = chatService.addChatRoom(roomName,username);
        return new ResponseEntity<>(addChatResponse, HttpStatus.OK);
    }

    @GetMapping("/chats")
    public ResponseEntity<ChatRoomsResponse> fetchChatRooms(@RequestParam Integer pageNum,@AuthenticationPrincipal String username){
        ChatRoomsResponse chatRoomsResponse = chatService.fetchChatRooms(pageNum,username);
        return new ResponseEntity<>(chatRoomsResponse, HttpStatus.OK);
    }

    @GetMapping("/chats/my")
    public ResponseEntity<ChatRoomsResponse> fetchMyChatRooms(@RequestParam Integer pageNum, @AuthenticationPrincipal String username){
        ChatRoomsResponse chatRoomsResponse = chatService.fetchMyChatRooms(pageNum,username);
        return new ResponseEntity<>(chatRoomsResponse, HttpStatus.OK);
    }
    @PostMapping("/chat/{chatId}")
    public ResponseEntity<ReadChatMessageResponse> readChatMessage(@PathVariable Long chatId, @AuthenticationPrincipal String username){
        ReadChatMessageResponse readChatMessageResponse = chatService.readChatMessage(chatId,username);
        return new ResponseEntity<>(readChatMessageResponse, HttpStatus.OK);
    }

    @DeleteMapping("/chat/{chatId}")
    public ResponseEntity<LeaveChatResponse> leaveChat(@PathVariable Long chatId,@AuthenticationPrincipal String username){
        LeaveChatResponse leaveChatResponse = chatService.leaveChat(chatId,username);
        return new ResponseEntity<>(leaveChatResponse, HttpStatus.OK);
    }



}
