package spring.myproject.dto.response.recommend;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import spring.myproject.dto.response.gathering.GatheringResponse;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendResponse {

    private String code;
    private String message;
    List<GatheringResponse> gatherings;
}
