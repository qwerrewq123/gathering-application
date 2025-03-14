package spring.myproject.dto.response.board;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardsResponse {

    private String code;
    private String message;
    private Page<BoardsQuery> page;

    public static BoardsResponse of(String code, String meesage, Page<BoardsQuery> page) {
        return BoardsResponse.builder()
                .code(code)
                .message(meesage)
                .page(page)
                .build();
    }
}
