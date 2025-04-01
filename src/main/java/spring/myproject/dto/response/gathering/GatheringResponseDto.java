package spring.myproject.dto.response.gathering;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GatheringResponseDto {
    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AddGatheringResponse {

        private String code;
        private String message;
        private Long id;
        public static AddGatheringResponse of(String code, String message,Long id) {
            return new AddGatheringResponse(code, message,id);
        }

    }
    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UpdateGatheringResponse {

        private String code;
        private String message;
        private Long id;
        public static UpdateGatheringResponse of(String code, String message,Long id) {
            return new UpdateGatheringResponse(code, message,id);
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GatheringResponse {

        private String code;
        private String message;
        private String title;
        private String content;
        private LocalDateTime registerDate;
        private String category;
        private String createdBy;
        private List<String> participatedBy;
        private List<String> participatedByNickname;
        private List<String> participatedByUrl;
        private String image;
        private int count;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GatheringCategoryResponse {

        private String code;
        private String message;
        List<GatheringsResponse> content;
        boolean hasNext;

        public static GatheringCategoryResponse of(String code, String message, List<GatheringsResponse> content, boolean hasNext) {
            return new GatheringCategoryResponse(code, message,content,hasNext);
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GatheringLikeResponse {

        private String code;
        private String message;
        List<GatheringsResponse> content;
        boolean hasNext;

        public static GatheringLikeResponse of(String code, String message, List<GatheringsResponse> content, boolean hasNext) {
            return new GatheringLikeResponse(code, message,content,hasNext);
        }

    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MainGatheringResponse {
        private String code;
        private String message;
        private Map<String, CategoryTotalGatherings> map = new HashMap<>();
    }


    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GatheringsResponse {

        private Long id;
        private String title;
        private String content;
        private LocalDateTime registerDate;
        private String category;
        private String createdBy;
        private String url;
        private int count;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MainGatheringElement {
        private Long id;
        private String title;
        private String content;
        private LocalDateTime registerDate;
        private String category;
        private String createdBy;
        private String url;
        private int count;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CategoryTotalGatherings {
        private List<MainGatheringElement> totalGatherings = new ArrayList<>();
        boolean hasNext;

    }
}
