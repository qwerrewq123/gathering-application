package spring.myproject.domain.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageRequest {
    @NotNull(message = "cannot null")
    private Long roomId;
    @NotBlank(message = "cannot blank or null or space")
    private String content;
    @NotBlank(message = "cannot blank or null or space")
    private String username;
}
