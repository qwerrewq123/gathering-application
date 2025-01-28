package spring.myproject.domain.gathering.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.domain.category.Category;
import spring.myproject.domain.category.repository.CategoryRepository;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.gathering.GatheringCount;
import spring.myproject.domain.gathering.repository.GatheringRepository;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;
import spring.myproject.dto.request.gathering.AddGatheringRequest;
import spring.myproject.dto.request.gathering.UpdateGatheringRequest;
import spring.myproject.dto.response.gathering.GatheringPagingQueryDto;
import spring.myproject.dto.response.gathering.GatheringPagingResponse;
import spring.myproject.dto.response.gathering.GatheringQueryDto;
import spring.myproject.dto.response.gathering.GatheringResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GatheringService {

    @Value("${file.dir}")
    private String fileDir;

    private final GatheringRepository gatheringRepository;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final GatheringCountService gatheringCountService;


    public void addGathering(AddGatheringRequest addGatheringRequest, MultipartFile file, String username) throws IOException {

        Image image = null;
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("해당하는 유저가 없습니다");
        }

        Category category = categoryRepository.findByName(addGatheringRequest.getCategory());
        if(category == null){
            throw new IllegalArgumentException("해당하는 카테고리가 업습니다");
        }


        if(!file.isEmpty()){
            String[] split = file.getOriginalFilename().split("\\.");
            String fullPath = fileDir+"/" + UUID.randomUUID()+"."+split[1];
            file.transferTo(new File(fullPath));
            image = Image.builder()
                    .url(fullPath)
                    .build();
            imageRepository.save(image);

        }


        Gathering gathering = Gathering.builder()
                .title(addGatheringRequest.getTitle())
                .content(addGatheringRequest.getContent())
                .createBy(user)
                .category(category)
                .registerDate(LocalDateTime.now())
                .gatheringImage(image)
                .build();

        gatheringCountService.makeCount(gathering);




        gatheringRepository.save(gathering);

    }

    public void updateGathering(UpdateGatheringRequest updateGatheringRequest, MultipartFile file, String username,Long gatheringId) throws IOException {

        Image image = null;
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("해당하는 유저가 없습니다");
        }

        Category category = categoryRepository.findByName(updateGatheringRequest.getCategory());
        if(category == null){
            throw new IllegalArgumentException("해당하는 카테고리가 업습니다");
        }

        //한번에 이미지 같이 가져와야함
        Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(()->{
            throw new IllegalArgumentException("해당하는 소모임이 없습니다");
        });



        if(!file.isEmpty()){

            Image preImage = gathering.getGatheringImage();
            String preUrl = preImage.getUrl();
            File preFile = new File(preUrl);
            preFile.delete();
            imageRepository.delete(preImage);


            String[] split = file.getOriginalFilename().split("\\.");
            String fullPath = fileDir+"/" + UUID.randomUUID()+"."+split[1];
            file.transferTo(new File(fullPath));
            image = Image.builder()
                    .url(fullPath)
                    .build();

            imageRepository.save(image);

        }

        gathering.setGatheringImage(image);
        gathering.setCategory(category);
        gathering.setContent(updateGatheringRequest.getContent());
        gathering.setTitle(updateGatheringRequest.getTitle());
        gathering.setRegisterDate(LocalDateTime.now());




    }

    public GatheringResponse gatheringDetail(Long gatheringId, String username) throws IOException {

        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("해당하는 유저가 없습니다");
        }

        List<GatheringQueryDto> gatheringQueryDtos = gatheringRepository.findGatheringAndCount(gatheringId);

        if(gatheringQueryDtos.size() == 0){
            throw new IllegalArgumentException("해당하는 소모임이 없습니다");
        }


        gatheringCountService.addCount(gatheringQueryDtos.getFirst().getId());



        return getGatheringResponse(gatheringQueryDtos);

    }

    public Page<GatheringPagingResponse> gatherings(int pageNum, String username,String title) {

        PageRequest pageRequest = PageRequest.of(pageNum - 1, 10, Sort.Direction.ASC,"id");
        Page<GatheringPagingQueryDto> gatheringPage = gatheringRepository.findPaging(pageRequest, title);
        return gatheringPage.map(
                g -> {
                    try {
                        return GatheringPagingResponse.builder()
                                .code("SU")
                                .message("Success")
                                .id(g.getId())
                                .title(g.getTitle())
                                .createdBy(g.getCreatedBy())
                                .registerDate(g.getRegisterDate())
                                .category(g.getCategory())
                                .content(g.getContent())
                                .image(encodeFileToBase64(g.getUrl()))
                                .count(g.getCount())
                                .build();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return GatheringPagingResponse.builder()
                                .code("FL")
                                .message("Fail")
                                .build();
                    }
                }
        );



    }

    public Page<GatheringPagingResponse> gatheringsLike(int pageNum, String username) {

        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("해당하는 유저가 없습니다");
        }

        PageRequest pageRequest = PageRequest.of(pageNum - 1, 10, Sort.Direction.ASC,"id");
        Long userId = user.getId();

        Page<GatheringPagingQueryDto> gatheringPage = gatheringRepository.findLikePaging(pageRequest, userId);

        return gatheringPage.map(
                g -> {
                    try {
                        return GatheringPagingResponse.builder()
                                .code("SU")
                                .message("Success")
                                .id(g.getId())
                                .title(g.getTitle())
                                .createdBy(g.getCreatedBy())
                                .registerDate(g.getRegisterDate())
                                .category(g.getCategory())
                                .content(g.getContent())
                                .image(encodeFileToBase64(g.getUrl()))
                                .count(g.getCount())
                                .build();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return GatheringPagingResponse.builder()
                                .code("FL")
                                .message("Fail")
                                .build();
                    }
                }
        );



    }

    public List<GatheringResponse> recommend(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("해당하는 유저가 없습니다");
        }
        List<GatheringQueryDto> gatheringQueryDtos = gatheringRepository.findRecommendPaging();

        return getGatheringResponses(gatheringQueryDtos);

    }





    private String encodeFileToBase64(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileBytes  = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(fileBytes);

    }

    private GatheringResponse getGatheringResponse(List<GatheringQueryDto> gatheringQueryDtos) throws IOException {

        GatheringResponse gatheringResponse = GatheringResponse.builder()
                .code("SU")
                .message("Success")
                .title(gatheringQueryDtos.getFirst().getTitle())
                .content(gatheringQueryDtos.getFirst().getContent())
                .registerDate(gatheringQueryDtos.getFirst().getRegisterDate())
                .category(gatheringQueryDtos.getFirst().getCategory())
                .createdBy(gatheringQueryDtos.getFirst().getCreatedBy())
                .image(encodeFileToBase64(gatheringQueryDtos.getFirst().getUrl()))
                .count(gatheringQueryDtos.getFirst().getCount()+1)
                .participatedBy(new ArrayList<>())
                .build();

        for (GatheringQueryDto gatheringQueryDto : gatheringQueryDtos) {
            if(StringUtils.hasText(gatheringQueryDto.getParticipatedBy())){
                gatheringResponse.getParticipatedBy().add(gatheringQueryDto.getParticipatedBy());
            }
        }

        return gatheringResponse;

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
}
