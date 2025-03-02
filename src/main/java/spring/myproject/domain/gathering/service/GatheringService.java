package spring.myproject.domain.gathering.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.domain.category.Category;
import spring.myproject.domain.category.repository.CategoryRepository;
import spring.myproject.domain.enrollment.Enrollment;
import spring.myproject.domain.enrollment.repository.EnrollmentRepository;
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
    private final EnrollmentRepository enrollmentRepository;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final S3ImageUploadService s3ImageUploadService;
    private final S3ImageDownloadService s3ImageDownloadService;

    public AddGatheringResponse addGathering(AddGatheringRequest addGatheringRequest, MultipartFile file, String username) throws IOException {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Category category = categoryRepository.findByName(addGatheringRequest.getCategory()).orElseThrow(()-> new NotFoundCategoryException("no exist Category!!"));
            Image image = null;
            image = saveImage(image,file);
            Gathering gathering = Gathering.of(addGatheringRequest,user,category,image);
            Enrollment enrollment = Enrollment.builder()
                        .gathering(gathering)
                        .accepted(true)
                        .date(LocalDateTime.now())
                        .enrolledBy(user)
                        .build();
            imageRepository.save(image);
            gatheringRepository.save(gathering);
            enrollmentRepository.save(enrollment);
            return AddGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public UpdateGatheringResponse updateGathering(UpdateGatheringRequest updateGatheringRequest, MultipartFile file, String username,Long gatheringId) throws IOException {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Category category = categoryRepository.findByName(updateGatheringRequest.getCategory()).orElseThrow(()-> new NotFoundCategoryException("no exist Category!!"));
            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(()->new NotFoundGatheringException("no exist Gathering!!"));
            boolean authorize = gathering.getCreateBy().getId() == user.getId();
            if(!authorize) throw new NotAuthorizeException("no authorize!!");
            Image image = null;
            image = saveImage(image,file);
            imageRepository.save(image);
            gathering.changeGathering(image,category,updateGatheringRequest);
            return UpdateGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public GatheringResponse gatheringDetail(Long gatheringId, String username) throws IOException {

            userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            List<GatheringDetailQuery> gatheringDetailQueries = gatheringRepository.gatheringDetail(gatheringId);
            if(gatheringDetailQueries.size() == 0) throw new NotFoundGatheringException("no exist Gathering!!!");
            return getGatheringResponse(gatheringDetailQueries);
    }

    public GatheringPagingResponse gatherings(int pageNum, String username, String title) {
            userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            PageRequest pageRequest = PageRequest.of(pageNum - 1, 8, Sort.Direction.ASC,"id");
            Page<GatheringsQuery> gatheringPage = gatheringRepository.gatherings(pageRequest, title);
            Page<GatheringsResponse> gatheringElementPage = gatheringPage.map(
                    g -> {
                        try {
                            return GatheringsResponse.builder()
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
                            return GatheringsResponse.builder()
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

    public GatheringPagingResponse gatheringCategory(String category, Integer pageNum, Integer pageSize, String username) {
            userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, Sort.Direction.ASC,"id");
            Page<GatheringsQuery> gatheringsQueryPage = gatheringRepository.gatheringsCategory(pageRequest,category);
            Page<GatheringsResponse> page = gatheringsQueryPage.map(g -> {
                GatheringsResponse.GatheringsResponseBuilder builder = GatheringsResponse.builder()
                        .id(g.getId())
                        .title(g.getTitle())
                        .createdBy(g.getCreatedBy())
                        .registerDate(g.getRegisterDate())
                        .category(g.getCategory())
                        .content(g.getContent())
                        .count(g.getCount());
                try {
                    String imageBase64 = s3ImageDownloadService.getFileBase64CodeFromS3(g.getUrl());
                    builder.image(imageBase64);
                } catch (IOException e) {
                    builder.image(null);
                }
                return builder.build();
            });
            return GatheringPagingResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,page);
    }

    public GatheringPagingResponse gatheringsLike(int pageNum, int pageSize, String username) {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, Sort.Direction.ASC,"id");
            Long userId = user.getId();
            Page<GatheringsQuery> gatheringsQueryPage = gatheringRepository.gatheringsLike(pageRequest, userId);
            Page<GatheringsResponse> page = gatheringsQueryPage.map(g -> {
                GatheringsResponse.GatheringsResponseBuilder builder = GatheringsResponse.builder()
                        .id(g.getId())
                        .title(g.getTitle())
                        .createdBy(g.getCreatedBy())
                        .registerDate(g.getRegisterDate())
                        .category(g.getCategory())
                        .content(g.getContent())
                        .count(g.getCount());
                try {
                    String imageBase64 = s3ImageDownloadService.getFileBase64CodeFromS3(g.getUrl());
                    builder.image(imageBase64);
                } catch (IOException e) {
                    builder.image(null);
                }
                return builder.build();
            });

        return GatheringPagingResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,page);
    }

    private Image saveImage(Image image,MultipartFile file) throws IOException {
        if(!file.isEmpty()){
            String url = s3ImageUploadService.upload(file);
            if(StringUtils.hasText(url)){
                image = Image.builder()
                        .url(url)
                        .build();
            }
        }
        return image;
    }
    private GatheringResponse getGatheringResponse(List<GatheringDetailQuery> gatheringDetailQueries) throws IOException {

        GatheringResponse gatheringResponse = GatheringResponse.builder()
                .code("SU")
                .message("Success")
                .title(gatheringDetailQueries.getFirst().getTitle())
                .content(gatheringDetailQueries.getFirst().getContent())
                .registerDate(gatheringDetailQueries.getFirst().getRegisterDate())
                .category(gatheringDetailQueries.getFirst().getCategory())
                .createdBy(gatheringDetailQueries.getFirst().getCreatedBy())
                .image(s3ImageDownloadService.getFileBase64CodeFromS3(gatheringDetailQueries.getFirst().getUrl()))
                .count(gatheringDetailQueries.getFirst().getCount())
                .participatedBy(new ArrayList<>())
                .build();

        for (GatheringDetailQuery gatheringDetailQuery : gatheringDetailQueries) {
            if(StringUtils.hasText(gatheringDetailQuery.getParticipatedBy())){
                gatheringResponse.getParticipatedBy().add(gatheringDetailQuery.getParticipatedBy());
            }
        }
        return gatheringResponse;
    }
}
