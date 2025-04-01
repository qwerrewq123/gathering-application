package spring.myproject.dto.request.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ChatRequestDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatMessageRequest {
        @NotNull
        private Long roomId;
        @NotBlank
        private String content;
        @NotBlank
        private String username;
    }
}
