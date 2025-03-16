package spring.myproject.service.recommend;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.repository.recommend.RecommendRepository;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.dto.response.gathering.GatheringDetailQuery;
import spring.myproject.dto.response.gathering.GatheringResponse;
import spring.myproject.dto.response.recommend.RecommendResponse;
import spring.myproject.exception.user.NotFoundUserException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static spring.myproject.utils.ConstClass.*;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendService {

    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;
    private final RecommendRepository recommendRepository;
    @Value("${server.url}")
    private String url;

    public void addScore(Gathering gathering,int val){
        recommendRepository.updateCount(gathering.getId(),val);
    }

    public RecommendResponse fetchRecommendTop10() {
            List<GatheringDetailQuery> gatheringQueryDtos = gatheringRepository.gatheringsRecommend();
            List<GatheringResponse> gatheringResponses = getGatheringResponses(gatheringQueryDtos);
            return RecommendResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,gatheringResponses);
    }

    private List<GatheringResponse> getGatheringResponses(List<GatheringDetailQuery> gatheringQueryDtos){
        Map<Long, List<GatheringDetailQuery>> groupedById = gatheringQueryDtos.stream()
                .collect(Collectors.groupingBy(GatheringDetailQuery::getId));

        return groupedById.values().stream()
                .map(group -> {
                    GatheringDetailQuery representative = group.get(0);
                    List<String> participatedBy = group.stream()
                            .map(GatheringDetailQuery::getParticipatedBy)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    GatheringResponse response = new GatheringResponse();
                    response.setCode("SU"); // 기본값 설정 (필요 시 변경)
                    response.setMessage("Success");
                    response.setTitle(representative.getTitle());
                    response.setContent(representative.getContent());
                    response.setRegisterDate(representative.getRegisterDate());
                    response.setCategory(representative.getCategory());
                    response.setCreatedBy(representative.getCreatedBy());
                    response.setParticipatedBy(participatedBy);
                    response.setCount(representative.getCount());
                    response.setImage(getUrl(representative.getUrl()));
                    return response;
                })
                .collect(Collectors.toList());
    }

    private String getUrl(String fileUrl){
        return url+fileUrl;
    }

}
