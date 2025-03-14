package spring.myproject.dto.response.gathering;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GatheringPagingResponse {

    private String code;
    private String message;
    Page<GatheringsResponse> gatheringElementPage;

    public static GatheringPagingResponse of(String code, String message,Page<GatheringsResponse> page) {
        return new GatheringPagingResponse(code, message,page);
    }

}
