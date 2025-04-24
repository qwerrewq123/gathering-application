package spring.myproject.dto.response.gathering.querydto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParticipatedQuery {
    private String participatedBy;
    private String participatedByNickname;
    private String participatedByUrl;
}
