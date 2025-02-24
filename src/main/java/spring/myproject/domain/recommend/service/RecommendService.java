package spring.myproject.domain.recommend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.myproject.domain.gathering.repository.GatheringRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;
import spring.myproject.domain.gathering.dto.response.GatheringQueryDto;
import spring.myproject.domain.gathering.dto.response.GatheringResponse;
import spring.myproject.domain.recommend.dto.response.RecommendResponse;
import spring.myproject.domain.user.exception.NotFoundUserException;
import spring.myproject.util.ConstClass;

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
public class RecommendService {

    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;

    public RecommendResponse recommend(String username) {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            List<GatheringQueryDto> gatheringQueryDtos = gatheringRepository.findRecommendPaging();
            List<GatheringResponse> gatheringResponses = getGatheringResponses(gatheringQueryDtos);
            return RecommendResponse.builder()
                    .code(SUCCESS_CODE)
                    .message(SUCCESS_MESSAGE)
                    .gatherings(gatheringResponses)
                    .build();
    }

    private List<GatheringResponse> getGatheringResponses(List<GatheringQueryDto> gatheringQueryDtos){
        Map<Long, List<GatheringQueryDto>> groupedById = gatheringQueryDtos.stream()
                .collect(Collectors.groupingBy(GatheringQueryDto::getId));

        return groupedById.values().stream()
                .map(group -> {
                    GatheringQueryDto representative = group.get(0);
                    List<String> participatedBy = group.stream()
                            .map(GatheringQueryDto::getParticipatedBy)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    GatheringResponse response = new GatheringResponse();
                    try {
                        response.setCode("SU"); // 기본값 설정 (필요 시 변경)
                        response.setMessage("Success");
                        response.setTitle(representative.getTitle());
                        response.setContent(representative.getContent());
                        response.setRegisterDate(representative.getRegisterDate());
                        response.setCategory(representative.getCategory());
                        response.setCreatedBy(representative.getCreatedBy());
                        response.setParticipatedBy(participatedBy);
                        response.setCount(representative.getCount());
                        response.setImage(encodeFileToBase64(representative.getUrl())); // 기본 이미지 설정 (필요 시 변경)
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return response;
                })
                .collect(Collectors.toList());
    }

    private String encodeFileToBase64(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileBytes  = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(fileBytes);

    }

}
