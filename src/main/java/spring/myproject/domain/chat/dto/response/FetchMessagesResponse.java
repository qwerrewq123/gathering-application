package spring.myproject.domain.chat.dto.response;

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
}
