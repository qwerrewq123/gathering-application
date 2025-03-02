package spring.myproject.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddChatRoomResponse {

    private String code;
    private String message;

    public static AddChatRoomResponse of(String code, String message){
        return new AddChatRoomResponse(code,message);
    }
}
