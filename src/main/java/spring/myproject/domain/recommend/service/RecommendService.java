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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static spring.myproject.util.UserConst.*;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;

    public RecommendResponse recommend(String username) {

        try {
            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            List<GatheringQueryDto> gatheringQueryDtos = gatheringRepository.findRecommendPaging();

            List<GatheringResponse> gatheringResponses = getGatheringResponses(gatheringQueryDtos);
            return RecommendResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .gatherings(gatheringResponses)
                    .build();



        }catch (NotFoundUserException e){
            return RecommendResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();

        }catch (Exception e){
            return RecommendResponse.builder()
                    .code(dbErrorCode)
                    .message(dbErrorMessage)
                    .build();
        }

    }

    private List<GatheringResponse> getGatheringResponses(List<GatheringQueryDto> gatheringQueryDtos){
        Map<Long, List<GatheringQueryDto>> groupedById = gatheringQueryDtos.stream()
                .collect(Collectors.groupingBy(GatheringQueryDto::getId));

        // 그룹화된 데이터를 GatheringResponse로 변환
        return groupedById.values().stream()
                .map(group -> {
                    // 동일한 id 그룹에서 대표가 될 첫 번째 요소
                    GatheringQueryDto representative = group.get(0);

                    // 참여자 목록 합치기
                    List<String> participatedBy = group.stream()
                            .map(GatheringQueryDto::getParticipatedBy)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());


                    // GatheringResponse 생성 및 매핑
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
                    ;

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
