package spring.myproject.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import spring.myproject.common.Username;
import spring.myproject.domain.chat.dto.response.*;
import spring.myproject.domain.chat.service.ChatService;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<AddChatRoomResponse> addChatRoom(@RequestParam String roomName, @Username String username){
        AddChatRoomResponse addChatResponse = chatService.addChatRoom(roomName,username);
        return new ResponseEntity<>(addChatResponse, HttpStatus.OK);
    }

    @GetMapping("/chats")
    public ResponseEntity<ChatRoomsResponse> fetchChatRooms(@RequestParam Integer pageNum,@Username String username){
        ChatRoomsResponse chatRoomsResponse = chatService.fetchChatRooms(pageNum,username);
        return new ResponseEntity<>(chatRoomsResponse, HttpStatus.OK);
    }

    @GetMapping("/chats/my")
    public ResponseEntity<ChatMyRoomsResponse> fetchMyChatRooms(@RequestParam Integer pageNum, @Username String username){
        ChatMyRoomsResponse chatMyRoomsResponse = chatService.fetchMyChatRooms(pageNum,username);
        return new ResponseEntity<>(chatMyRoomsResponse, HttpStatus.OK);
    }

    @GetMapping("/messages/{chatId}")
    public ResponseEntity<FetchMessagesResponse> fetchMessages(@PathVariable Long chatId,@Username String username){
        FetchMessagesResponse fetchMessagesResponse = chatService.fetchMessages(chatId,username);
        return new ResponseEntity<>(fetchMessagesResponse, HttpStatus.OK);
    }

    @PostMapping("/chat/{chatId}")
    public ResponseEntity<ReadChatMessageResponse> readChatMessage(@PathVariable Long chatId, @Username String username){
        ReadChatMessageResponse readChatMessageResponse = chatService.readChatMessage(chatId,username);
        return new ResponseEntity<>(readChatMessageResponse, HttpStatus.OK);
    }

    @DeleteMapping("/chat/{chatId}")
    public ResponseEntity<LeaveChatResponse> leaveChat(@PathVariable Long chatId,@Username String username){
        LeaveChatResponse leaveChatResponse = chatService.leaveChat(chatId,username);
        return new ResponseEntity<>(leaveChatResponse, HttpStatus.OK);
    }

    @PostMapping("/attend/chat/{chatId}")
    public ResponseEntity<AttendChatResponse> attendChat(@RequestParam Long chatId, @Username String username){
        AttendChatResponse attendChatResponse = chatService.attendChat(chatId,username);
        return new ResponseEntity<>(attendChatResponse, HttpStatus.OK);
    }



}
