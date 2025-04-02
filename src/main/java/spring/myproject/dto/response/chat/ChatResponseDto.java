package spring.myproject.dto.response.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.myproject.dto.response.chat.query.ChatMessageElement;

import java.util.List;

public class ChatResponseDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AddChatRoomResponse {

        private String code;
        private String message;
        private Long id;
        public static AddChatRoomResponse of(String code, String message,Long id){
            return new AddChatRoomResponse(code,message,id);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LeaveChatResponse {
        String code;
        String message;

        public static LeaveChatResponse of(String code, String message) {
            return new LeaveChatResponse(code, message);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReadChatMessageResponse {
        private String code;
        private String message;

        public static ReadChatMessageResponse of(String code, String message) {
            return new ReadChatMessageResponse(code, message);
        }
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class ChatMessagesResponse {
        private String code;
        private String message;
        private List<ChatMessageElement> content;

        public static ChatMessagesResponse of(String code, String message, List<ChatMessageElement> content) {
            return new ChatMessagesResponse(code, message, content);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AttendChatResponse {
        private String code;
        private String message;

        public static AttendChatResponse of(String code, String message) {
            return new AttendChatResponse(code, message);
        }
    }

}
