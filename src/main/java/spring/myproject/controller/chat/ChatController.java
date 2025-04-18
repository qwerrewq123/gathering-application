package spring.myproject.controller.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.myproject.common.annotation.Id;
import spring.myproject.service.chat.ChatService;

import static spring.myproject.dto.response.chat.ChatResponseDto.*;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private ChatService chatService;

    @PostMapping("/gathering/{gatheringId}/chat")
    public ResponseEntity<AddChatRoomResponse> addChatRoom(@RequestParam String roomName, @Id Long userId){
        AddChatRoomResponse addChatResponse = chatService.addChatRoom(roomName,userId);
        return new ResponseEntity<>(addChatResponse, HttpStatus.OK);
    }

    @GetMapping("/messages/{chatId}")
    public ResponseEntity<ChatMessagesResponse> fetchMessages(@PathVariable Long chatId, @Id Long userId){
        ChatMessagesResponse chatMessagesResponse = chatService.fetchMessages(chatId,userId);
        return new ResponseEntity<>(chatMessagesResponse, HttpStatus.OK);
    }

    @DeleteMapping("/chat/{chatId}")
    public ResponseEntity<LeaveChatResponse> leaveChat(@PathVariable Long chatId, @Id Long userId){
        LeaveChatResponse leaveChatResponse = chatService.leaveChat(chatId,userId);
        return new ResponseEntity<>(leaveChatResponse, HttpStatus.OK);
    }

    @PostMapping("/gathering/{gatheringId}/chat/{chatId}/attend")
    public ResponseEntity<AttendChatResponse> attendChat(@RequestParam Long chatId, @Id Long userId){
        AttendChatResponse attendChatResponse = chatService.attendChat(chatId,userId);
        return new ResponseEntity<>(attendChatResponse, HttpStatus.OK);
    }

    @PostMapping("/chat/{chatId}")
    public ResponseEntity<ReadChatMessageResponse> readChatMessage(@PathVariable Long chatId, @Id Long userId){
        ReadChatMessageResponse readChatMessageResponse = chatService.readChatMessage(chatId,userId);
        return new ResponseEntity<>(readChatMessageResponse, HttpStatus.OK);
    }

    @GetMapping("/gathering/{gatheringId}/chats")
    public ResponseEntity<ChatRoomResponse> fetchChatRooms(@RequestParam Integer pageNum, @Id Long userId){
        ChatRoomResponse chatRoomResponse = chatService.fetchChatRooms(pageNum,userId);
        return new ResponseEntity<>(chatRoomResponse, HttpStatus.OK);
    }

    @GetMapping("/gathering/{gatheringId}//my/chats")
    public ResponseEntity<MyChatRoomResponse> fetchMyChatRooms(@RequestParam Integer pageNum, @Id Long userId){
        MyChatRoomResponse myChatRoomResponse = chatService.fetchMyChatRooms(pageNum,userId);
        return new ResponseEntity<>(myChatRoomResponse, HttpStatus.OK);
    }



}
