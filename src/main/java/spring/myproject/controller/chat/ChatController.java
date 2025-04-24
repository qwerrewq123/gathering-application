package spring.myproject.controller.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.myproject.common.annotation.Id;
import spring.myproject.service.chat.ChatService;

import static spring.myproject.dto.request.chat.ChatRequestDto.*;
import static spring.myproject.dto.response.chat.ChatResponseDto.*;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/my/chats")
    public ResponseEntity<MyChatRoomResponse> fetchMyChatRooms(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @Id Long userId){
        MyChatRoomResponse myChatRoomResponse = chatService.fetchMyChatRooms(pageNum,pageSize,userId);
        return new ResponseEntity<>(myChatRoomResponse, HttpStatus.OK);
    }

    @GetMapping("/gathering/{gatheringId}/chats")
    public ResponseEntity<ChatRoomResponse> fetchChatRooms(@PathVariable Long gatheringId, @RequestParam Integer pageNum, @RequestParam Integer pageSize, @Id Long userId){
        ChatRoomResponse chatRoomResponse = chatService.fetchChatRooms(gatheringId,pageNum,pageSize,userId);
        return new ResponseEntity<>(chatRoomResponse, HttpStatus.OK);
    }
    @GetMapping("/gathering/{gatheringId}/able/chats")
    public ResponseEntity<AbleChatRoomResponse> fetchAbleChatRooms(@PathVariable Long gatheringId, @RequestParam Integer pageNum, @RequestParam Integer pageSize, @Id Long userId){
        AbleChatRoomResponse ableChatRoomResponse = chatService.fetchAbleChatRooms(gatheringId,pageNum,pageSize,userId);
        return new ResponseEntity<>(ableChatRoomResponse, HttpStatus.OK);
    }

    @GetMapping("/gathering/{gatheringId}/participate/chats")
    public ResponseEntity<ParticipateChatRoomResponse> fetchParticipateChatRooms(@PathVariable Long gatheringId, @RequestParam Integer pageNum, @RequestParam Integer pageSize, @Id Long userId){
        ParticipateChatRoomResponse participateChatRoomResponse = chatService.fetchParticipateChatRooms(gatheringId,pageNum,pageSize,userId);
        return new ResponseEntity<>(participateChatRoomResponse, HttpStatus.OK);
    }

    @PostMapping("/gathering/{gatheringId}/chat")
    public ResponseEntity<AddChatRoomResponse> addChatRoom(@PathVariable Long gatheringId, @RequestBody AddChatRequest addChatRequest, @Id Long userId){
        AddChatRoomResponse addChatResponse = chatService.addChatRoom(gatheringId,addChatRequest,userId);
        return new ResponseEntity<>(addChatResponse, HttpStatus.OK);
    }

    @PostMapping("/chat/attend/{chatId}")
    public ResponseEntity<AttendChatResponse> attendChat(@PathVariable Long chatId, @Id Long userId){
        AttendChatResponse attendChatResponse = chatService.attendChat(chatId,userId);
        return new ResponseEntity<>(attendChatResponse, HttpStatus.OK);
    }

    @PostMapping("/chat/disAttend/{chatId}")
    public ResponseEntity<LeaveChatResponse> leaveChat(@PathVariable Long chatId, @Id Long userId){
        LeaveChatResponse leaveChatResponse = chatService.leaveChat(chatId,userId);
        return new ResponseEntity<>(leaveChatResponse, HttpStatus.OK);
    }

    @GetMapping("/messages/{chatId}")
    public ResponseEntity<ChatMessagesResponse> fetchMessages(@PathVariable Long chatId, @Id Long userId){
        ChatMessagesResponse chatMessagesResponse = chatService.fetchUnReadMessages(chatId,userId);
        return new ResponseEntity<>(chatMessagesResponse, HttpStatus.OK);
    }

    @PostMapping("/chat/{chatId}")
    public ResponseEntity<ReadChatMessageResponse> readChatMessage(@PathVariable Long chatId, @Id Long userId){
        ReadChatMessageResponse readChatMessageResponse = chatService.readChatMessage(chatId,userId);
        return new ResponseEntity<>(readChatMessageResponse, HttpStatus.OK);
    }



}
