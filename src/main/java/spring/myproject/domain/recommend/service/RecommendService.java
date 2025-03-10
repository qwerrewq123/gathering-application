package spring.myproject.domain.recommend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import spring.myproject.domain.gathering.repository.GatheringRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;
import spring.myproject.domain.gathering.dto.response.GatheringDetailQuery;
import spring.myproject.domain.gathering.dto.response.GatheringResponse;
import spring.myproject.domain.recommend.dto.response.RecommendResponse;
import spring.myproject.domain.user.exception.NotFoundUserException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static spring.myproject.util.ConstClass.*;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendService {

    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;
    @Value("${server.url}")
    private String url;

    public RecommendResponse recommend(String username) {

            userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
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
