package spring.myproject.dto.response.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FetchMessagesResponse {
    private String code;
    private String message;
    private List<ChatMessageResponse> chatMessageResponses = new ArrayList<>();

    public static FetchMessagesResponse of(String code, String message,List<ChatMessageResponse> chatMessageResponses) {
        return new FetchMessagesResponse(code, message, chatMessageResponses);
    }
}
