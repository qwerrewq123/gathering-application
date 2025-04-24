package spring.myproject.dto.response.chat.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public  class AbleChatRoomElement {
    private Long id;
    private String chatRoomTitle;
    private String description;
    private int count;
    private String createdBy;
    private String gatheringTitle;
}