package spring.myproject.service.meeting;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.dto.response.meeting.querydto.MeetingDetailQuery;
import spring.myproject.dto.response.meeting.querydto.MeetingsQuery;
import spring.myproject.entity.attend.Attend;
import spring.myproject.dto.response.meeting.*;
import spring.myproject.repository.attend.AttendRepository;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.entity.image.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.meeting.Meeting;
import spring.myproject.repository.meeting.MeetingRepository;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.exception.gathering.NotFoundGatheringException;
import spring.myproject.exception.meeting.MeetingIsNotEmptyException;
import spring.myproject.exception.meeting.NotAuthorizeException;
import spring.myproject.exception.meeting.NotFoundMeetingExeption;
import spring.myproject.exception.user.NotFoundUserException;
import spring.myproject.s3.S3ImageUploadService;
import spring.myproject.utils.change.ChangeFactory;

import java.io.IOException;
import java.time.LocalDateTime;
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
    @Value("${server.url}")
    private String url;
    public AddMeetingResponse addMeeting(AddMeetingRequest addMeetingRequest, String username, Long gatheringId, MultipartFile file) throws IOException {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(() -> new NotFoundGatheringException("no exist Gathering!!"));
            Image image = null;
            image = saveImage(image,file);
            Meeting meeting = Meeting.of(addMeetingRequest,image,user,gathering);
            Attend attend = Attend.of(meeting,user);
            if(image!=null) imageRepository.save(image);
            meetingRepository.save(meeting);
            attendRepository.save(attend);
            return AddMeetingResponse.of(SUCCESS_CODE, SUCCESS_MESSAGE, meeting.getId());
    }

    public DeleteMeetingResponse deleteMeeting(String username, Long meetingId) {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeetingExeption("no exist Meeting!!"));
            boolean authorize = meeting.getCreatedBy().getId() == user.getId();
            if(authorize == false){
                throw new NotAuthorizeException("no authority!");
            }
            if(meeting.getCount() > 1){
                throw new MeetingIsNotEmptyException("meeting is not empty!!");
            }
            Attend attend = attendRepository.findByUserIdAndMeetingIdAndTrue(user.getId(), meetingId);
            attendRepository.delete(attend);
            meetingRepository.delete(meeting);
            return DeleteMeetingResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public UpdateMeetingResponse updateMeeting(UpdateMeetingRequest updateMeetingRequest, String username, Long meetingId, MultipartFile file) throws IOException {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeetingExeption("no exist Meeting!!"));
            boolean authorize = meeting.getCreatedBy().getId() == user.getId();
            if(authorize == false){
                throw new NotAuthorizeException("no authority!");
            }
            Image image = null;
            image = saveImage(image,file);
            if(image!=null) imageRepository.save(image);
            changeMeeting(meeting,updateMeetingRequest,image);
            return UpdateMeetingResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,meetingId);
    }
    public MeetingResponse meetingDetail(Long meetingId, String username) {

        userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
        List<MeetingDetailQuery> meetingDetailQueries = meetingRepository.meetingDetail(meetingId);
        if(meetingDetailQueries.size() == 0) throw new NotFoundMeetingExeption("no exist Meeting!!");
        List<String> attends = attendedBy(meetingDetailQueries);
        String url = getUrl(meetingDetailQueries.getFirst().getUrl());
        return MeetingResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,meetingDetailQueries,attends,url);

    }

    public MeetingsResponse meetings(int pageNum, int pageSize, String username, @RequestParam(defaultValue = "") String title) {

            userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, Sort.Direction.ASC,"id");
            Page<MeetingsQuery> page = meetingRepository.meetings(pageRequest,title);
            List<MeetingElement> content = toContent(page);
            boolean hasNext = page.hasNext();
            return MeetingsResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,content,hasNext);
    }

    private List<String> attendedBy(List<MeetingDetailQuery> meetingDetailQueries){

        List<String> attendedBy = new ArrayList<>();
        for (MeetingDetailQuery meetingQueryResponse : meetingDetailQueries) {
            if(StringUtils.hasText(meetingQueryResponse.getAttendedBy())){
                attendedBy.add(meetingQueryResponse.getAttendedBy());
            }
        }
        return attendedBy;
    }

    private List<MeetingElement> toContent(Page<MeetingsQuery> page) {
            return page.map(query -> MeetingElement.from(query,(fileUrl)->(fileUrl+url)))
                    .getContent();
    }

    private Image saveImage(Image image, MultipartFile file) throws IOException {
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
