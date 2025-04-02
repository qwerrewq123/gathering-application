package spring.myproject.controller.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.myproject.common.Username;
import spring.myproject.dto.response.chat.*;
import spring.myproject.service.chat.ChatService;

import static spring.myproject.dto.response.chat.ChatResponseDto.*;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<AddChatRoomResponse> addChatRoom(@RequestParam String roomName, @Username String username){
        AddChatRoomResponse addChatResponse = chatService.addChatRoom(roomName,username);
        return new ResponseEntity<>(addChatResponse, HttpStatus.OK);
    }

    @GetMapping("/messages/{chatId}")
    public ResponseEntity<ChatMessagesResponse> fetchMessages(@PathVariable Long chatId, @Username String username){
        ChatMessagesResponse chatMessagesResponse = chatService.fetchMessages(chatId,username);
        return new ResponseEntity<>(chatMessagesResponse, HttpStatus.OK);
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

    @PostMapping("/chat/{chatId}")
    public ResponseEntity<ReadChatMessageResponse> readChatMessage(@PathVariable Long chatId, @Username String username){
        ReadChatMessageResponse readChatMessageResponse = chatService.readChatMessage(chatId,username);
        return new ResponseEntity<>(readChatMessageResponse, HttpStatus.OK);
    }

    @GetMapping("/chats")
    public ResponseEntity<ChatRoomResponse> fetchChatRooms(@RequestParam Integer pageNum, @Username String username){
        ChatRoomResponse chatRoomResponse = chatService.fetchChatRooms(pageNum,username);
        return new ResponseEntity<>(chatRoomResponse, HttpStatus.OK);
    }

    @GetMapping("/chats/my")
    public ResponseEntity<MyChatRoomResponse> fetchMyChatRooms(@RequestParam Integer pageNum, @Username String username){
        MyChatRoomResponse myChatRoomResponse = chatService.fetchMyChatRooms(pageNum,username);
        return new ResponseEntity<>(myChatRoomResponse, HttpStatus.OK);
    }



}
