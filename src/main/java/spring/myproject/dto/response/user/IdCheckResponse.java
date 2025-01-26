package spring.myproject.dto.response.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IdCheckResponse {

    private final String code;
    private final String message;

}
