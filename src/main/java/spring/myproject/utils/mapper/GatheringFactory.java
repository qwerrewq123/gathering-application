package spring.myproject.utils.mapper;

import spring.myproject.common.functional.MyFunctionalInterface;
import spring.myproject.dto.response.gathering.GatheringResponseDto;
import spring.myproject.dto.response.gathering.querydto.GatheringDetailQuery;

import java.util.ArrayList;
import java.util.List;

import static spring.myproject.dto.response.gathering.GatheringResponseDto.*;

public class GatheringFactory {

    public static GatheringResponse toGatheringResponse(List<GatheringDetailQuery> gatheringDetailQueries,boolean hasNext,
                                                        MyFunctionalInterface functionalInterface) {
        return GatheringResponse.builder()
                .code("SU")
                .message("Success")
                .title(gatheringDetailQueries.getFirst().getTitle())
                .hasNext(hasNext)
                .content(gatheringDetailQueries.getFirst().getContent())
                .registerDate(gatheringDetailQueries.getFirst().getRegisterDate())
                .category(gatheringDetailQueries.getFirst().getCategory())
                .createdBy(gatheringDetailQueries.getFirst().getCreatedBy())
                .createdByUrl(gatheringDetailQueries.getFirst().getCreatedByUrl())
                .imageUrl(functionalInterface.execute(gatheringDetailQueries.getFirst().getUrl()))
                .count(gatheringDetailQueries.getFirst().getCount())
                .participatedByList(new ArrayList<>())
                .build();
    }
}
