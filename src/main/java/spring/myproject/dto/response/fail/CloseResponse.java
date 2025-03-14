package spring.myproject.dto.response.fail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CloseResponse {
    private String code;
    private String message;

    public static CloseResponse of(String code, String message) {
        return CloseResponse.builder()
                .code(code)
                .message(message)
                .build();
    }
}
