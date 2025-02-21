package spring.myproject.domain.gathering.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
import spring.myproject.dto.response.gathering.*;
import spring.myproject.exception.category.NotFoundCategoryException;
import spring.myproject.exception.gathering.NotFoundGatheringException;
import spring.myproject.exception.user.NotFoundUserException;
import spring.myproject.util.CategoryConst;
import spring.myproject.util.GatheringConst;
import spring.myproject.util.ImageConst;
import spring.myproject.util.UserConst;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static spring.myproject.util.UserConst.*;

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

    public AddGatheringResponse addGathering(AddGatheringRequest addGatheringRequest, MultipartFile file, String username){

        Image image = null;
        User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));


        Category category = categoryRepository.findByName(addGatheringRequest.getCategory()).orElseThrow(()-> new NotFoundCategoryException("no exist Category!!"));


        try {

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

            return AddGatheringResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();

        }catch (NotFoundUserException e){
            return AddGatheringResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();

        }catch (NotFoundCategoryException e){
            return AddGatheringResponse.builder()
                    .code(CategoryConst.notFoundCode)
                    .message(CategoryConst.notFoundMessage)
                    .build();

        }catch (IOException e){
            return AddGatheringResponse.builder()
                    .code(ImageConst.uploadFailCode)
                    .message(ImageConst.uploadFailMessage)
                    .build();
        }


    }
    //TODO : gathering 권한 로직 추가
    public UpdateGatheringResponse updateGathering(UpdateGatheringRequest updateGatheringRequest, MultipartFile file, String username,Long gatheringId){


        try {

            Image image = null;
            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));

            Category category = categoryRepository.findByName(updateGatheringRequest.getCategory()).orElseThrow(()-> new NotFoundCategoryException("no exist Category!!"));

            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(()->new NotFoundGatheringException("no exist Gathering!!"));

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

            return UpdateGatheringResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();

        }catch (NotFoundUserException e){
            return UpdateGatheringResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();

        }catch (NotFoundCategoryException e){
            return UpdateGatheringResponse.builder()
                    .code(CategoryConst.notFoundCode)
                    .message(CategoryConst.notFoundMessage)
                    .build();

        }catch (NotFoundGatheringException e){
            return UpdateGatheringResponse.builder()
                    .code(GatheringConst.notFoundGatheringCode)
                    .message(GatheringConst.notFoundGatheringMessage)
                    .build();


        }catch (IOException e){
            return UpdateGatheringResponse.builder()
                    .code(ImageConst.uploadFailCode)
                    .message(ImageConst.uploadFailMessage)
                    .build();
        }

    }

    public GatheringResponse gatheringDetail(Long gatheringId, String username) throws IOException {

        try {
            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));

            List<GatheringQueryDto> gatheringQueryDtos = gatheringRepository.findGatheringAndCount(gatheringId);

            if(gatheringQueryDtos.size() == 0){
                throw new NotFoundGatheringException("no exist Gathering!!!");
            }


            gatheringCountService.addCount(gatheringQueryDtos.getFirst().getId());



            return getGatheringResponse(gatheringQueryDtos);

        }catch (NotFoundUserException e){
        return GatheringResponse.builder()
                .code(notFoundCode)
                .message(notFoundMessage)
                .build();

        }catch (NotFoundCategoryException e){
        return GatheringResponse.builder()
                .code(CategoryConst.notFoundCode)
                .message(CategoryConst.notFoundMessage)
                .build();

        }catch (NotFoundGatheringException e){
        return GatheringResponse.builder()
                .code(GatheringConst.notFoundGatheringCode)
                .message(GatheringConst.notFoundGatheringMessage)
                .build();


        }catch (IOException e){
        return GatheringResponse.builder()
                .code(ImageConst.uploadFailCode)
                .message(ImageConst.uploadFailMessage)
                .build();
    }

    }

    public GatheringPagingResponse gatherings(int pageNum, String username,String title) {


        try {
            PageRequest pageRequest = PageRequest.of(pageNum - 1, 10, Sort.Direction.ASC,"id");
            Page<GatheringPagingQueryDto> gatheringPage = gatheringRepository.findPaging(pageRequest, title);
            Page<GatheringElement> gatheringElementPage = gatheringPage.map(
                    g -> {
                        try {
                            return GatheringElement.builder()
                                    .id(g.getId())
                                    .title(g.getTitle())
                                    .createdBy(g.getCreatedBy())
                                    .registerDate(g.getRegisterDate())
                                    .category(g.getCategory())
                                    .content(g.getContent())
                                    .image(encodeFileToBase64(g.getUrl()))
                                    .count(g.getCount())
                                    .build();
                        } catch (IOException e) {
                            return GatheringElement.builder()
                                    .id(g.getId())
                                    .title(g.getTitle())
                                    .createdBy(g.getCreatedBy())
                                    .registerDate(g.getRegisterDate())
                                    .category(g.getCategory())
                                    .content(g.getContent())
                                    .count(g.getCount())
                                    .build();
                        }
                    }
            );

            return GatheringPagingResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .gatheringElementPage(gatheringElementPage)
                    .build();

        }catch (Exception e){
            return GatheringPagingResponse.builder()
                    .code(dbErrorCode)
                    .message(dbErrorMessage)
                    .build();
        }



    }

    public GatheringPagingResponse gatheringsLike(int pageNum, String username) {


        try {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));

            PageRequest pageRequest = PageRequest.of(pageNum - 1, 10, Sort.Direction.ASC,"id");
            Long userId = user.getId();

            Page<GatheringPagingQueryDto> gatheringPage = gatheringRepository.findLikePaging(pageRequest, userId);

            Page<GatheringElement> gatheringElementPage = gatheringPage.map(
                    g -> {
                        try {
                            return GatheringElement.builder()
                                    .id(g.getId())
                                    .title(g.getTitle())
                                    .createdBy(g.getCreatedBy())
                                    .registerDate(g.getRegisterDate())
                                    .category(g.getCategory())
                                    .content(g.getContent())
                                    .image(encodeFileToBase64(g.getUrl()))
                                    .count(g.getCount())
                                    .build();
                        } catch (IOException e) {
                            return GatheringElement.builder()
                                    .id(g.getId())
                                    .title(g.getTitle())
                                    .createdBy(g.getCreatedBy())
                                    .registerDate(g.getRegisterDate())
                                    .category(g.getCategory())
                                    .content(g.getContent())
                                    .count(g.getCount())
                                    .build();
                        }
                    }
            );

            return GatheringPagingResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .gatheringElementPage(gatheringElementPage)
                    .build();

        }catch (Exception e){
            return GatheringPagingResponse.builder()
                    .code(dbErrorCode)
                    .message(dbErrorMessage)
                    .build();
        }



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


}
