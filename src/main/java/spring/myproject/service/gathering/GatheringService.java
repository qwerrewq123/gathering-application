package spring.myproject.service.gathering;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.domain.Category;
import spring.myproject.dto.response.gathering.*;
import spring.myproject.repository.category.CategoryRepository;
import spring.myproject.domain.Enrollment;
import spring.myproject.repository.enrollment.EnrollmentRepository;
import spring.myproject.domain.Gathering;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.domain.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.domain.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.dto.request.gathering.AddGatheringRequest;
import spring.myproject.dto.request.gathering.UpdateGatheringRequest;
import spring.myproject.exception.category.NotFoundCategoryException;
import spring.myproject.exception.gathering.NotFoundGatheringException;
import spring.myproject.exception.meeting.NotAuthorizeException;
import spring.myproject.exception.user.NotFoundUserException;
import spring.myproject.s3.S3ImageDownloadService;
import spring.myproject.s3.S3ImageUploadService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
            if(image!=null) imageRepository.save(image);
            gathering.changeGathering(image,category,updateGatheringRequest);
            return UpdateGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public GatheringResponse gatheringDetail(Long gatheringId, String username) throws IOException {

            userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            List<GatheringDetailQuery> gatheringDetailQueries = gatheringRepository.gatheringDetail(gatheringId);
            if(gatheringDetailQueries.isEmpty()) throw new NotFoundGatheringException("no exist Gathering!!!");
            return getGatheringResponse(gatheringDetailQueries);
    }

    public TotalGatheringsResponse gatherings(String username, String title) {
            userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            List<EntireGatheringsQuery> entireGatheringsQueries = gatheringRepository.gatherings(title);
            List<GatheringsQuery> gatherings = toGatheringQueriesList(entireGatheringsQueries);
            List<TotalGatheringsElement> totalGatheringsElements = toGatheringsResponseList(gatherings);
            Map<String, CategoryTotalGatherings> map = categorizeByCategory(totalGatheringsElements);
            return createTotalGatherings(map);

    }

    public GatheringPagingResponse gatheringCategory(String category, Integer pageNum, Integer pageSize, String username) {
        userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, Sort.Direction.ASC,"id");
        Page<GatheringsQuery> gatheringsQueryPage = gatheringRepository.gatheringsCategory(pageRequest,category);
        Page<GatheringsResponse> page = toGatheringsResponsePage(gatheringsQueryPage);
        return GatheringPagingResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,page);
    }

    public GatheringPagingResponse gatheringsLike(int pageNum, int pageSize, String username) {

        User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, Sort.Direction.DESC,"id");
        Long userId = user.getId();
        Page<GatheringsQuery> gatheringsQueryPage = gatheringRepository.gatheringsLike(pageRequest, userId);
        Page<GatheringsResponse> page = toGatheringsResponsePage(gatheringsQueryPage);
        return GatheringPagingResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,page);
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
                .image(getUrl(gatheringDetailQueries.getFirst().getUrl()))
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
    private List<GatheringsQuery> toGatheringQueriesList(List<EntireGatheringsQuery> entireGatheringsQueries) {
        return entireGatheringsQueries.stream()
                .map(query -> GatheringsQuery.builder()
                        .id(query.getId())
                        .title(query.getTitle())
                        .content(query.getContent())
                        .registerDate(query.getRegisterDate())
                        .category(query.getCategory())
                        .createdBy(query.getCreatedBy())
                        .url(query.getUrl())
                        .count(query.getCount())
                        .build())
                .collect(Collectors.toList());
    }

    private List<TotalGatheringsElement> toGatheringsResponseList(List<GatheringsQuery> gatheringsQueryList) {
        return gatheringsQueryList.stream()
                .map(this::toGatheringsResponse)
                .collect(Collectors.toList());
    }
    public Map<String, CategoryTotalGatherings> categorizeByCategory(List<TotalGatheringsElement> totalGatheringsElements) {
        return totalGatheringsElements.stream()
                .collect(Collectors.groupingBy(
                        TotalGatheringsElement::getCategory,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                this::processCategoryElements
                        )
                ));
    }
    private TotalGatheringsElement toGatheringsResponse(GatheringsQuery gatheringsQuery) {

        return TotalGatheringsElement.builder().id(gatheringsQuery.getId())
                .title(gatheringsQuery.getTitle())
                .content(gatheringsQuery.getContent())
                .registerDate(gatheringsQuery.getRegisterDate())
                .category(gatheringsQuery.getCategory())
                .createdBy(gatheringsQuery.getCreatedBy())
                .count(gatheringsQuery.getCount())
                .url(gatheringsQuery.getUrl())
                .build();

    }

    private CategoryTotalGatherings processCategoryElements(List<TotalGatheringsElement> elements) {
        boolean hasNext = elements.size() >= 9;
        if (hasNext) {
            elements = elements.subList(0, 8);
        }

        return CategoryTotalGatherings.builder()
                .totalGatherings(elements)
                .hasNext(hasNext)
                .build();
    }
    private TotalGatheringsResponse createTotalGatherings(Map<String, CategoryTotalGatherings> categoryMap) {
        return TotalGatheringsResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .map(categoryMap)
                .build();
    }


    private Page<GatheringsResponse> toGatheringsResponsePage(Page<GatheringsQuery> page) {
        return page.map(g ->
                GatheringsResponse.builder()
                        .id(g.getId())
                        .title(g.getTitle())
                        .createdBy(g.getCreatedBy())
                        .registerDate(g.getRegisterDate())
                        .category(g.getCategory())
                        .content(g.getContent())
                        .count(g.getCount())
                        .url(getUrl(g.getUrl()))
                        .build()
        );
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

    private String getUrl(String fileUrl){
        return url+fileUrl;
    }
}
