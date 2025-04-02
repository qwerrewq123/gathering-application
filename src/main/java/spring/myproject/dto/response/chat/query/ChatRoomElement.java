package spring.myproject.dto.response.chat.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomElement {
    private Long id;
    private String name;
    private int count;
    private String createdBy;
    private boolean status;
}
