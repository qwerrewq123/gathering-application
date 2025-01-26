package spring.myproject.dto.response.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailCertificationResponse {

    private String code;
    private String message;
}
