package spring.myproject.service.gathering;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.dto.response.gathering.querydto.MainGatheringsQuery;
import spring.myproject.dto.response.gathering.querydto.GatheringDetailQuery;
import spring.myproject.dto.response.gathering.querydto.GatheringsQuery;
import spring.myproject.entity.category.Category;
import spring.myproject.entity.fcm.Topic;
import spring.myproject.utils.mapper.GatheringFactory;
import spring.myproject.repository.category.CategoryRepository;
import spring.myproject.entity.enrollment.Enrollment;
import spring.myproject.repository.enrollment.EnrollmentRepository;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.repository.fcm.TopicRepository;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.common.exception.category.NotFoundCategoryException;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.common.exception.meeting.NotAuthorizeException;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.common.s3.S3ImageUploadService;
import spring.myproject.service.fcm.FCMService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static spring.myproject.dto.request.gathering.GatheringRequestDto.*;
import static spring.myproject.dto.response.gathering.GatheringResponseDto.*;
import static spring.myproject.utils.ConstClass.*;
import static spring.myproject.utils.RandomStringGenerator.*;

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
    private final FCMService fcmService;
    private final TopicRepository topicRepository;
    @Value("${server.url}")
    private String url;

    public AddGatheringResponse addGathering(AddGatheringRequest addGatheringRequest, MultipartFile file, String username) throws IOException {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Category category = categoryRepository.findByName(addGatheringRequest.getCategory()).orElseThrow(()-> new NotFoundCategoryException("no exist Category!!"));
            Image image = null;
            image = saveImage(image,file);
            Gathering gathering = Gathering.of(addGatheringRequest,user,category,image);
            Enrollment enrollment = Enrollment.of(true, gathering, user, LocalDateTime.now());
            if(image!=null) imageRepository.save(image);
            gatheringRepository.save(gathering);
            enrollmentRepository.save(enrollment);
            Topic topic = Topic.builder()
                .topicName(generateRandomString())
                .build();
            topicRepository.save(topic);
            fcmService.subscribeToTopics(topic.getTopicName(),username);
            return AddGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE, gathering.getId());
    }

    public UpdateGatheringResponse updateGathering(UpdateGatheringRequest updateGatheringRequest, MultipartFile file, String username,Long gatheringId) throws IOException {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Category category = categoryRepository.findByName(updateGatheringRequest.getCategory()).orElseThrow(()-> new NotFoundCategoryException("no exist Category!!"));
            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(()->new NotFoundGatheringException("no exist Gathering!!"));
            boolean authorize = gathering.getCreateBy().getId() == user.getId();
            if(!authorize) throw new NotAuthorizeException("no authorize!!");
            Image image = null;
            image = saveImage(image,file);
            if(image!=null) imageRepository.save(image);
            gathering.changeGathering(image,category,updateGatheringRequest);
            return UpdateGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,gatheringId);
    }

    public GatheringResponse gatheringDetail(Long gatheringId, String username) throws IOException {

            userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            List<GatheringDetailQuery> gatheringDetailQueries = gatheringRepository.gatheringDetail(gatheringId);
            if(gatheringDetailQueries.isEmpty()) throw new NotFoundGatheringException("no exist Gathering!!!");
            return getGatheringResponse(gatheringDetailQueries);
    }

    public GatheringCategoryResponse gatheringCategory(String category, Integer pageNum, Integer pageSize, String username) {
        userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, Sort.Direction.ASC,"id");
        Page<GatheringsQuery> page = gatheringRepository.gatheringsCategory(pageRequest,category);
        boolean hasNext = page.hasNext();
        List<GatheringsResponse> content = toContent(page);
        return GatheringCategoryResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,content,hasNext);
    }

    public GatheringLikeResponse gatheringsLike(int pageNum, int pageSize, String username) {

        User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, Sort.Direction.DESC,"id");
        Long userId = user.getId();
        Page<GatheringsQuery> page = gatheringRepository.gatheringsLike(pageRequest, userId);
        boolean hasNext = page.hasNext();
        List<GatheringsResponse> content = toContent(page);
        return GatheringLikeResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,content,hasNext);
    }

    public MainGatheringResponse gatherings(String username, String title) {
            userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            List<MainGatheringsQuery> mainGatheringsQueryList = gatheringRepository.gatherings(title);
            List<GatheringsQuery> gatherings = toGatheringQueriesList(mainGatheringsQueryList);
            List<MainGatheringElement> mainGatheringElements = toGatheringsResponseList(gatherings);
            Map<String, CategoryTotalGatherings> map = categorizeByCategory(mainGatheringElements);
            return toMainGatheringResponse(map);

    }

    private GatheringResponse getGatheringResponse(List<GatheringDetailQuery> gatheringDetailQueries){

        GatheringResponse gatheringResponse = GatheringFactory.toGatheringResponse(gatheringDetailQueries
                ,(fileUrl)->url+fileUrl);
        for (GatheringDetailQuery gatheringDetailQuery : gatheringDetailQueries) {
            if(StringUtils.hasText(gatheringDetailQuery.getParticipatedBy())){
                gatheringResponse.getParticipatedByUrl().add(gatheringDetailQuery.getParticipatedBy());
            }
            if(StringUtils.hasText(gatheringDetailQuery.getParticipatedByNickname())){
                gatheringResponse.getParticipatedByNickname().add(gatheringDetailQuery.getParticipatedByNickname());
            }
            if(StringUtils.hasText(gatheringDetailQuery.getParticipatedByUrl())){
                gatheringResponse.getParticipatedByUrl().add(gatheringDetailQuery.getParticipatedByUrl());
            }
        }
        return gatheringResponse;
    }

    private List<GatheringsResponse> toContent(Page<GatheringsQuery> page) {

        return page.map(g->GatheringsResponse.from(g,(fileUrl)->url+fileUrl))
                .getContent();
    }

    private List<GatheringsQuery> toGatheringQueriesList(List<MainGatheringsQuery> mainGatheringsQueries) {
        return mainGatheringsQueries.stream()
                .map(query -> GatheringsQuery.of(query))
                .collect(Collectors.toList());
    }

    private List<MainGatheringElement> toGatheringsResponseList(List<GatheringsQuery> gatheringsQueryList) {
        return gatheringsQueryList.stream()
                .map(this::toGatheringsResponse)
                .collect(Collectors.toList());
    }
    public Map<String, CategoryTotalGatherings> categorizeByCategory(List<MainGatheringElement> mainGatheringElements) {
        return mainGatheringElements.stream()
                .collect(Collectors.groupingBy(
                        MainGatheringElement::getCategory,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                this::processCategoryElements
                        )
                ));
    }
    private MainGatheringElement toGatheringsResponse(GatheringsQuery gatheringsQuery) {

        return MainGatheringElement.from(gatheringsQuery, (fileUrl)->url+fileUrl);
    }

    private CategoryTotalGatherings processCategoryElements(List<MainGatheringElement> elements) {
        boolean hasNext = elements.size() >= 9;
        if (hasNext) {
            elements = elements.subList(0, 8);
        }

        return CategoryTotalGatherings.builder()
                .totalGatherings(elements)
                .hasNext(hasNext)
                .build();
    }

    private MainGatheringResponse toMainGatheringResponse(Map<String, CategoryTotalGatherings> categoryMap) {
        return MainGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,categoryMap);
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

}
