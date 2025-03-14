package spring.myproject.dto.response.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadChatMessageResponse {
    private String code;
    private String message;

    public static ReadChatMessageResponse of(String code, String message) {
        return new ReadChatMessageResponse(code, message);
    }
}
