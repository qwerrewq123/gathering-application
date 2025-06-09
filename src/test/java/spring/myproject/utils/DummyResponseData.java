package spring.myproject.utils;

import spring.myproject.dto.response.gathering.GatheringResponseDto;

import static spring.myproject.dto.response.gathering.GatheringResponseDto.*;
import static spring.myproject.utils.ConstClass.*;

public class DummyResponseData {
    public static MainGatheringResponse retMainGatheringResponse(){
        return MainGatheringResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .build();
    }

    public static GatheringCategoryResponse retGatheringCategoryResponse(){
        return GatheringCategoryResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .build();
    }

    public static GatheringResponse retGatheringResponse(){
        return GatheringResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .build();
    }

    public static GatheringLikeResponse retGatheringLikeResponse(){
        return GatheringLikeResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .build();
    }

}
