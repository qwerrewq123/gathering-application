package spring.myproject.service.recommend;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import spring.myproject.dto.response.gathering.GatheringResponseDto;
import spring.myproject.dto.response.gathering.querydto.GatheringDetailQuery;
import spring.myproject.dto.response.gathering.querydto.GatheringsQuery;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.repository.recommend.RecommendRepository;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.dto.response.recommend.RecommendResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static spring.myproject.dto.response.gathering.GatheringResponseDto.*;
import static spring.myproject.utils.ConstClass.*;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendService {

    private final GatheringRepository gatheringRepository;
    private final RecommendRepository recommendRepository;
    @Value("${server.url}")
    private String url;

    public void addScore(Gathering gathering,int val){
        recommendRepository.updateCount(gathering.getId(),val);
    }
    @Cacheable(value = "recommend",key="#localDate")
    public RecommendResponse fetchRecommendTop10(LocalDate localDate) {
            List<GatheringsQuery> gatheringsQueries = gatheringRepository.gatheringsRecommend(localDate);
        List<GatheringsResponse> content = gatheringsQueries.stream().map(query -> GatheringsResponse.from(query, (fileUrl) -> (url + fileUrl)))
                .toList();
        return RecommendResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,content);
    }

}
