package spring.myproject.domain.gathering.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.domain.category.Category;
import spring.myproject.domain.category.repository.CategoryRepository;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.gathering.repository.GatheringRepository;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;
import spring.myproject.dto.request.gathering.AddGatheringRequest;
import spring.myproject.dto.request.gathering.UpdateGatheringRequest;
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
                .participatedBy(new ArrayList<>())
                .createBy(user)
                .category(category)
                .registerDate(LocalDateTime.now())
                .gatheringImage(image)
                .build();

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
        Gathering gathering = gatheringRepository.findById(gatheringId).orElse(null);
        if(gathering == null){
            throw new IllegalArgumentException("해당하는 소모임이 없습니다");
        }


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

        Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(() -> {
            throw new IllegalArgumentException("해당하는 소모임이 없습니다");
        });

        String category = gathering.getCategory().getName();
        String createdby = gathering.getCreateBy().getUsername();
        List<String> participatedBy = getParticipatedBy(gathering);
        String image = encodeFileToBase64(gathering.getGatheringImage().getUrl());

        return GatheringResponse.builder()
                .code("SU")
                .message("Success")
                .title(gathering.getTitle())
                .createdBy(createdby)
                .registerDate(gathering.getRegisterDate())
                .participatedBy(participatedBy)
                .category(category)
                .content(gathering.getContent())
                .image(image)
                .build();


    }

    public Page<GatheringResponse> gatherings(int pageNum, String username,String title) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, 10, Sort.Direction.ASC);
        Page<Gathering> gatheringPage = gatheringRepository.findPaging(pageRequest, title);
        Page<GatheringResponse> gatheringResponsePage = gatheringPage.map(
                g -> {
                    try {
                        return GatheringResponse.builder()
                                .code("SU")
                                .message("Success")
                                .title(g.getTitle())
                                .createdBy(g.getCreateBy().getUsername())
                                .registerDate(g.getRegisterDate())
                                .category(g.getCategory().getName())
                                .content(g.getContent())
                                .image(encodeFileToBase64(g.getGatheringImage().getUrl()))
                                .participatedBy(getParticipatedBy(g))
                                .build();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return GatheringResponse.builder()
                                .code("FL")
                                .message("Fail")
                                .build();
                    }
                }
        );

        return gatheringResponsePage;

    }

    public Page<GatheringResponse> gatheringsLike(int pageNum, String username) {

        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("해당하는 유저가 없습니다");
        }

        PageRequest pageRequest = PageRequest.of(pageNum - 1, 10, Sort.Direction.ASC);
        Long userId = user.getId();

        Page<Gathering> gatheringPage = gatheringRepository.findLikePaging(pageRequest, userId);

        Page<GatheringResponse> gatheringResponsePage = gatheringPage.map(
                g -> {
                    try {
                        return GatheringResponse.builder()
                                .code("SU")
                                .message("Success")
                                .title(g.getTitle())
                                .createdBy(g.getCreateBy().getUsername())
                                .registerDate(g.getRegisterDate())
                                .category(g.getCategory().getName())
                                .content(g.getContent())
                                .image(encodeFileToBase64(g.getGatheringImage().getUrl()))
                                .participatedBy(getParticipatedBy(g))
                                .build();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return GatheringResponse.builder()
                                .code("FL")
                                .message("Fail")
                                .build();
                    }
                }
        );

        return gatheringResponsePage;

    }

    public List<GatheringResponse> recommend(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("해당하는 유저가 없습니다");
        }
        List<Gathering> gatheringList = gatheringRepository.findRecommendPaging();
        List<GatheringResponse> gatheringResponseList = gatheringList.stream()
                .map(g -> {
                    try {
                        return GatheringResponse.builder()
                                .code("SU")
                                .message("Success")
                                .title(g.getTitle())
                                .createdBy(g.getCreateBy().getUsername())
                                .registerDate(g.getRegisterDate())
                                .category(g.getCategory().getName())
                                .content(g.getContent())
                                .image(encodeFileToBase64(g.getGatheringImage().getUrl()))
                                .participatedBy(getParticipatedBy(g))
                                .build();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return GatheringResponse.builder()
                                .code("FL")
                                .message("Fail")
                                .build();
                    }
                }).collect(Collectors.toList());


        return gatheringResponseList;
    }



    private  List<String> getParticipatedBy(Gathering gathering) {
        List<String> participatedBy = new ArrayList<>();
        for (User participatedUser : gathering.getParticipatedBy()) {
            participatedBy.add(participatedUser.getUsername());

        }
        return participatedBy;
    }

    private String encodeFileToBase64(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileBytes  = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(fileBytes);

    }
}
