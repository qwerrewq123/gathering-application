package spring.myproject.dto.response.gathering;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TotalGatheringsResponse {
    private String code;
    private String message;
    private Map<String,CategoryTotalGatherings> map = new HashMap<>();
}
