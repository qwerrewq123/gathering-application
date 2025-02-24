package spring.myproject.domain.gathering.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.domain.category.Category;
import spring.myproject.domain.category.repository.CategoryRepository;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.gathering.dto.response.*;
import spring.myproject.domain.gathering.repository.GatheringRepository;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;
import spring.myproject.domain.gathering.dto.request.AddGatheringRequest;
import spring.myproject.domain.gathering.dto.request.UpdateGatheringRequest;
import spring.myproject.domain.category.exception.NotFoundCategoryException;
import spring.myproject.domain.gathering.exception.NotFoundGatheringException;
import spring.myproject.domain.meeting.exception.NotAuthorizeException;
import spring.myproject.domain.user.exception.NotFoundUserException;
import spring.myproject.s3.S3ImageDownloadService;
import spring.myproject.s3.S3ImageUploadService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static spring.myproject.util.ConstClass.*;

@Service
@Transactional
@RequiredArgsConstructor
public class GatheringService {

    private final GatheringRepository gatheringRepository;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final GatheringCountService gatheringCountService;
    private final S3ImageUploadService s3ImageUploadService;
    private final S3ImageDownloadService s3ImageDownloadService;

    public AddGatheringResponse addGathering(AddGatheringRequest addGatheringRequest, MultipartFile file, String username){

        try {
            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Category category = categoryRepository.findByName(addGatheringRequest.getCategory()).orElseThrow(()-> new NotFoundCategoryException("no exist Category!!"));
            Image image = null;
            String url = s3ImageUploadService.upload(file);
            if(StringUtils.hasText(url)){
                image = Image.builder()
                        .url(url)
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
                    .code(SUCCESS_CODE)
                    .message(SUCCESS_MESSAGE)
                    .build();
        }catch (NotFoundUserException e){
            return AddGatheringResponse.builder()
                    .code(NOT_FOUND_USER_CODE)
                    .message(NOT_FOUND_USER_MESSAGE)
                    .build();
        }catch (NotFoundCategoryException e){
            return AddGatheringResponse.builder()
                    .code(NOT_FOUND_GATHERING_CODE)
                    .message(NOT_FOUND_CATEGORY_MESSAGE)
                    .build();
        }catch (IOException e){
            return AddGatheringResponse.builder()
                    .code(UPLOAD_FAIL_CODE)
                    .message(UPLOAD_FAIL_MESSAGE)
                    .build();
        }
    }
    public UpdateGatheringResponse updateGathering(UpdateGatheringRequest updateGatheringRequest, MultipartFile file, String username,Long gatheringId){

        try {
            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Category category = categoryRepository.findByName(updateGatheringRequest.getCategory()).orElseThrow(()-> new NotFoundCategoryException("no exist Category!!"));
            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(()->new NotFoundGatheringException("no exist Gathering!!"));
            boolean authorize = gathering.getCreateBy().getId() == user.getId();
            if(!authorize) throw new NotAuthorizeException("no authorize!!");
            Image image = null;
            String url = s3ImageUploadService.upload(file);
            if(StringUtils.hasText(url)){
                image = Image.builder()
                        .url(url)
                        .build();
                imageRepository.save(image);
            }
            gathering.setGatheringImage(image);
            gathering.setCategory(category);
            gathering.setContent(updateGatheringRequest.getContent());
            gathering.setTitle(updateGatheringRequest.getTitle());
            gathering.setRegisterDate(LocalDateTime.now());
            return UpdateGatheringResponse.builder()
                    .code(SUCCESS_CODE)
                    .message(SUCCESS_MESSAGE)
                    .build();
        }catch (NotFoundUserException e){
            return UpdateGatheringResponse.builder()
                    .code(NOT_FOUND_USER_CODE)
                    .message(NOT_FOUND_USER_MESSAGE)
                    .build();
        }catch (NotFoundCategoryException e){
            return UpdateGatheringResponse.builder()
                    .code(NOT_FOUND_CATEGORY_CODE)
                    .message(NOT_FOUND_CATEGORY_MESSAGE)
                    .build();
        }catch (NotFoundGatheringException e){
            return UpdateGatheringResponse.builder()
                    .code(NOT_FOUND_GATHERING_CODE)
                    .message(NOT_FOUND_GATHERING_MESSAGE)
                    .build();
        }catch (NotAuthorizeException e){
            return UpdateGatheringResponse.builder()
                    .code(NOT_AUTHORIZE_CODE)
                    .message(NOT_AUTHORIZED_MESSAGE)
                    .build();
        }
        catch (IOException e){
            return UpdateGatheringResponse.builder()
                    .code(UPLOAD_FAIL_CODE)
                    .message(UPLOAD_FAIL_MESSAGE)
                    .build();
        }
    }

    public GatheringResponse gatheringDetail(Long gatheringId, String username){

        try {
            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            List<GatheringQueryDto> gatheringQueryDtos = gatheringRepository.findGatheringAndCount(gatheringId);
            if(gatheringQueryDtos.size() == 0) throw new NotFoundGatheringException("no exist Gathering!!!");
            gatheringCountService.addCount(gatheringQueryDtos.getFirst().getId());
            return getGatheringResponse(gatheringQueryDtos);
        }catch (NotFoundUserException e){
        return GatheringResponse.builder()
                .code(NOT_FOUND_USER_CODE)
                .message(NOT_FOUND_USER_MESSAGE)
                .build();
        }catch (NotFoundCategoryException e){
        return GatheringResponse.builder()
                .code(NOT_FOUND_CATEGORY_CODE)
                .message(NOT_FOUND_CATEGORY_MESSAGE)
                .build();
        }catch (NotFoundGatheringException e){
        return GatheringResponse.builder()
                .code(NOT_FOUND_GATHERING_CODE)
                .message(NOT_FOUND_GATHERING_MESSAGE)
                .build();
        }catch (IOException e){
            System.out.println(e.getMessage());
        return GatheringResponse.builder()
                .code(UPLOAD_FAIL_CODE)
                .message(UPLOAD_FAIL_MESSAGE)
                .build();
        }
    }

    public GatheringPagingResponse gatherings(int pageNum, String username, String title) {

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
                                    .image(s3ImageDownloadService.getFileBase64CodeFromS3(g.getUrl()))
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
                    .code(SUCCESS_CODE)
                    .message(SUCCESS_MESSAGE)
                    .gatheringElementPage(gatheringElementPage)
                    .build();
        }catch (Exception e){
            return GatheringPagingResponse.builder()
                    .code(DB_ERROR_CODE)
                    .message(DB_ERROR_MESSAGE)
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
                                    .image(s3ImageDownloadService.getFileBase64CodeFromS3(g.getUrl()))
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
                    .code(SUCCESS_CODE)
                    .message(SUCCESS_MESSAGE)
                    .gatheringElementPage(gatheringElementPage)
                    .build();
        }catch (Exception e){
            return GatheringPagingResponse.builder()
                    .code(DB_ERROR_CODE)
                    .message(DB_ERROR_MESSAGE)
                    .build();
        }
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
                .image(s3ImageDownloadService.getFileBase64CodeFromS3(gatheringQueryDtos.getFirst().getUrl()))
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
