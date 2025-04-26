package spring.myproject.dto.request.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.user.User;

public class ChatRequestDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddChatRequest {
        @NotBlank
        private String title;
        @NotBlank
        private String description;

        public static ChatRoom toChatRoom(AddChatRequest addChatRequest, User user, Gathering gathering){
            return ChatRoom.builder()
                    .title(addChatRequest.getTitle())
                    .description(addChatRequest.getDescription())
                    .count(1)
                    .createdBy(user)
                    .gathering(gathering)
                    .build();
        }

    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatMessageRequest {
        @NotBlank
        private String content;
        @NotBlank
        private Long userId;
        @NotBlank
        private String username;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatMessageResponse {
        @NotBlank
        private String content;
        @NotBlank
        private Long userId;
        @NotBlank
        private String username;
    }
}
