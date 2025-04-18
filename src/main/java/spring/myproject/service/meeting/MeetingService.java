package spring.myproject.service.meeting;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.dto.response.meeting.querydto.MeetingDetailQuery;
import spring.myproject.dto.response.meeting.querydto.MeetingsQuery;
import spring.myproject.entity.attend.Attend;
import spring.myproject.repository.attend.AttendRepository;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.meeting.Meeting;
import spring.myproject.repository.meeting.MeetingRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.common.exception.meeting.NotAuthorizeException;
import spring.myproject.common.exception.meeting.NotFoundMeetingExeption;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.common.s3.S3ImageUploadService;
import spring.myproject.service.recommend.RecommendService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static spring.myproject.dto.request.meeting.MeetingRequestDto.*;
import static spring.myproject.dto.response.meeting.MeetingResponseDto.*;
import static spring.myproject.utils.ConstClass.*;
import static spring.myproject.utils.change.ChangeFactory.*;


@Service
@Transactional
@RequiredArgsConstructor
public class MeetingService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final GatheringRepository gatheringRepository;
    private final AttendRepository attendRepository;
    private final S3ImageUploadService s3ImageUploadService;
    private final ImageRepository imageRepository;
    private final RecommendService recommendService;
    @Value("${server.url}")
    private String url;
    public AddMeetingResponse addMeeting(AddMeetingRequest addMeetingRequest, Long userId, Long gatheringId, MultipartFile file) throws IOException {

            User user = userRepository.findById(userId).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(() -> new NotFoundGatheringException("no exist Gathering!!"));
            Image image = null;
            image = saveImage(image,file);
            Meeting meeting = Meeting.of(addMeetingRequest,image,user,gathering);
            Attend attend = Attend.of(meeting,user);
            if(image!=null) imageRepository.save(image);
            meetingRepository.save(meeting);
            attendRepository.save(attend);
            recommendService.addScore(gatheringId,1);
            return AddMeetingResponse.of(SUCCESS_CODE, SUCCESS_MESSAGE, meeting.getId());
    }

    public DeleteMeetingResponse deleteMeeting(Long userId, Long meetingId,Long gatheringId) {

            userRepository.findById(userId).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeetingExeption("no exist Meeting!!"));
            boolean authorize = meeting.getCreatedBy().getId().equals(userId);
            if(!authorize) throw new NotAuthorizeException("no authority!");
            meetingRepository.delete(meeting);
            recommendService.addScore(gatheringId,-1);
            return DeleteMeetingResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public UpdateMeetingResponse updateMeeting(UpdateMeetingRequest updateMeetingRequest, Long userId, Long meetingId, MultipartFile file,Long gatheringId) throws IOException {

            User user = userRepository.findById(userId).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeetingExeption("no exist Meeting!!"));
            boolean authorize = meeting.getCreatedBy().getId() == user.getId();
            if(!authorize) throw new NotAuthorizeException("no authority!");
            Image image = null;
            image = saveImage(image,file);
            if(image!=null) imageRepository.save(image);
            changeMeeting(meeting,updateMeetingRequest,image);
            return UpdateMeetingResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,meetingId);
    }
    public MeetingResponse meetingDetail(Long meetingId, Long userId, Long gatheringId) {

        userRepository.findById(userId).orElseThrow(()->new NotFoundUserException("no exist User!!"));
        List<MeetingDetailQuery> meetingDetailQueries = meetingRepository.meetingDetail(meetingId);
        if(meetingDetailQueries.isEmpty()) throw new NotFoundMeetingExeption("no exist Meeting!!");
        return toMeetingResponse(meetingDetailQueries);
    }

    public MeetingsResponse meetings(int pageNum, int pageSize, Long userId, String title) {

            userRepository.findById(userId).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, Sort.Direction.ASC,"id");
            Page<MeetingsQuery> page = meetingRepository.meetings(pageRequest,title);
            List<MeetingElement> content = toContent(page);
            boolean hasNext = page.hasNext();
            return MeetingsResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,content,hasNext);
    }

    private MeetingResponse toMeetingResponse(List<MeetingDetailQuery> meetingDetailQueries) {
        List<String> attendBy = meetingDetailQueries.stream().map(MeetingDetailQuery::getAttendedBy).toList();
        List<String> attendByNickname = meetingDetailQueries.stream().map(MeetingDetailQuery::getAttendByNickname).toList();
        List<String> attendByUrl = meetingDetailQueries.stream().map(query -> url + query.getAttendedByUrl()).toList();

        return MeetingResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .id(meetingDetailQueries.getFirst().getId())
                .title(meetingDetailQueries.getFirst().getTitle())
                .createdBy(meetingDetailQueries.getFirst().getCreatedBy())
                .createdByNickname(meetingDetailQueries.getFirst().getCreatedByNickname())
                .createdByUrl(url+meetingDetailQueries.getFirst().getCreatedByUrl())
                .boardDate(meetingDetailQueries.getFirst().getBoardDate())
                .startDate(meetingDetailQueries.getFirst().getStartDate())
                .endDate(meetingDetailQueries.getFirst().getEndDate())
                .meetingUrl(url+meetingDetailQueries.getFirst().getUrl())
                .attendedBy(attendBy)
                .attendedByNickname(attendByNickname)
                .attendedByUrl(attendByUrl)
                .build();


    }



    private List<MeetingElement> toContent(Page<MeetingsQuery> page) {
            return page.map(query -> MeetingElement.from(query,(fileUrl)->(url+fileUrl)))
                    .getContent();
    }

    private Image saveImage(Image image, MultipartFile file) throws IOException {
        if(!file.isEmpty()){
            String url = s3ImageUploadService.upload(file);
            String contentType = file.getContentType();
            if(StringUtils.hasText(url)){
                image = Image.builder()
                        .url(url)
                        .contentType(contentType)
                        .build();
            }
        }
        return image;
    }

}
