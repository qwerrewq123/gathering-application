package spring.myproject.dto.response.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaveChatResponse {
    String code;
    String message;

    public static LeaveChatResponse of(String code, String message) {
        return new LeaveChatResponse(code, message);
    }
}
