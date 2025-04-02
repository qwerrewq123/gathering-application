package spring.myproject.dto.response.board;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import spring.myproject.dto.response.board.querydto.BoardQuery;
import spring.myproject.dto.response.board.querydto.BoardsQuery;

import java.time.LocalDateTime;
import java.util.List;

public class BoardResponseDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AddBoardResponse {

        private String code;
        private String message;
        private Long id;
        public static AddBoardResponse of(String successCode, String successMessage,Long id) {
            return new AddBoardResponse(successCode,successMessage,id);
        }
    }

    @Data
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BoardResponse {
        private String code;
        private String message;
        private String title;
        private String description;
        private List<String> imageUrls;
        private String username;
        private String userImageUrl;
        private LocalDateTime registerDate;

        public static BoardResponse of(List<BoardQuery> boardQueries, List<String> imageUrls, String userImageUrl , String code, String message) {
            return BoardResponse.builder()
                    .code(code)
                    .message(message)
                    .title(boardQueries.getFirst().getTitle())
                    .description(boardQueries.getFirst().getDescription())
                    .registerDate(boardQueries.getFirst().getRegisterDate())
                    .imageUrls(imageUrls)
                    .username(boardQueries.getFirst().getUsername())
                    .userImageUrl(userImageUrl)
                    .build();

        }
    }

    @Data
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BoardsResponse {

        private String code;
        private String message;
        private List<BoardElement> content;
        boolean hasNext;

        public static BoardsResponse of(String code, String message, List<BoardElement> content,boolean hasNext) {
            return BoardsResponse.builder()
                    .code(code)
                    .message(message)
                    .content(content)
                    .hasNext(hasNext)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoardElement {
        private String title;
        private String description;
        private String username;
        private String content;
        private LocalDateTime registerDate;

        public static BoardElement from(BoardsQuery boardsQuery){
            return BoardElement.builder()
                    .title(boardsQuery.getTitle())
                    .description(boardsQuery.getDescription())
                    .username(boardsQuery.getUsername())
                    .content(boardsQuery.getContent())
                    .registerDate(boardsQuery.getRegisterDate())
                    .build();


        }
    }
}
