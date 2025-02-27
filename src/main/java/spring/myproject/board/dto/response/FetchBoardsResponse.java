package spring.myproject.board.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FetchBoardsResponse {

    private String code;
    private String message;
    private Page<QueryBoardDto> page;

    public static FetchBoardsResponse of(String code, String meesage, Page<QueryBoardDto> page) {
        return FetchBoardsResponse.builder()
                .code(code)
                .message(meesage)
                .page(page)
                .build();
    }
}
