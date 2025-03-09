package spring.myproject.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMyRoomResponse {
    private String name;
    private int count;
    private String createdBy;
    private boolean status;
    private long unReadCount;
}
