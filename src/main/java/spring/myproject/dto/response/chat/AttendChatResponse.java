package spring.myproject.dto.response.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendChatResponse {
    private String code;
    private String message;

    public static AttendChatResponse of(String code, String message) {
        return new AttendChatResponse(code, message);
    }
}
