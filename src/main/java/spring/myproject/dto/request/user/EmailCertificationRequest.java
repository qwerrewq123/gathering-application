package spring.myproject.dto.request.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailCertificationRequest {

    private String email;
}
