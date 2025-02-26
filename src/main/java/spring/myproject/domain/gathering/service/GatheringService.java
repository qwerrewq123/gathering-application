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

    public AddGatheringResponse addGathering(AddGatheringRequest addGatheringRequest, MultipartFile file, String username) throws IOException {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Category category = categoryRepository.findByName(addGatheringRequest.getCategory()).orElseThrow(()-> new NotFoundCategoryException("no exist Category!!"));
            Image image = null;
            if(!file.isEmpty()){
                String url = s3ImageUploadService.upload(file);
                if(StringUtils.hasText(url)){
                    image = Image.builder()
                            .url(url)
                            .build();
                }
            }
            imageRepository.save(image);
            Gathering gathering = Gathering.of(addGatheringRequest,user,category,image);
            gatheringCountService.makeCount(gathering);
            gatheringRepository.save(gathering);
            return AddGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public UpdateGatheringResponse updateGathering(UpdateGatheringRequest updateGatheringRequest, MultipartFile file, String username,Long gatheringId) throws IOException {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Category category = categoryRepository.findByName(updateGatheringRequest.getCategory()).orElseThrow(()-> new NotFoundCategoryException("no exist Category!!"));
            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(()->new NotFoundGatheringException("no exist Gathering!!"));
            boolean authorize = gathering.getCreateBy().getId() == user.getId();
            if(!authorize) throw new NotAuthorizeException("no authorize!!");
            Image image = null;
            if(!file.isEmpty()){
                String url = s3ImageUploadService.upload(file);
                if(StringUtils.hasText(url)){
                    image = Image.builder()
                            .url(url)
                            .build();
                }
            }
            imageRepository.save(image);
            gathering.changeGathering(image,category,updateGatheringRequest);
            return UpdateGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public GatheringResponse gatheringDetail(Long gatheringId, String username) throws IOException {

            userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            List<GatheringQueryDto> gatheringQueryDtos = gatheringRepository.findGatheringAndCount(gatheringId);
            if(gatheringQueryDtos.size() == 0) throw new NotFoundGatheringException("no exist Gathering!!!");
            gatheringCountService.addCount(gatheringQueryDtos.getFirst().getId());
            return getGatheringResponse(gatheringQueryDtos);
    }

    public GatheringPagingResponse gatherings(int pageNum, String username, String title) {

            PageRequest pageRequest = PageRequest.of(pageNum - 1, 8, Sort.Direction.ASC,"id");
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
            return GatheringPagingResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,gatheringElementPage);
    }

    public GatheringPagingResponse gatheringsLike(int pageNum, String username) {

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
            return GatheringPagingResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,gatheringElementPage);
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
