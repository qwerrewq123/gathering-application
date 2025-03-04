package spring.myproject.domain.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.domain.attend.Attend;
import spring.myproject.domain.attend.repository.AttendRepository;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.gathering.repository.GatheringRepository;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.meeting.Meeting;
import spring.myproject.domain.meeting.dto.response.*;
import spring.myproject.domain.meeting.repository.MeetingRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;
import spring.myproject.domain.meeting.dto.request.AddMeetingRequest;
import spring.myproject.domain.meeting.dto.request.UpdateMeetingRequest;
import spring.myproject.domain.gathering.exception.NotFoundGatheringException;
import spring.myproject.domain.meeting.exception.MeetingIsNotEmptyException;
import spring.myproject.domain.meeting.exception.NotAuthorizeException;
import spring.myproject.domain.meeting.exception.NotFoundMeetingExeption;
import spring.myproject.domain.user.exception.NotFoundUserException;
import spring.myproject.s3.S3ImageDownloadService;
import spring.myproject.s3.S3ImageUploadService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static spring.myproject.util.ConstClass.*;


@Service
@Transactional
@RequiredArgsConstructor
public class MeetingService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final GatheringRepository gatheringRepository;
    private final AttendRepository attendRepository;
    private final S3ImageUploadService s3ImageUploadService;
    private final S3ImageDownloadService s3ImageDownloadService;
    private final ImageRepository imageRepository;
    public AddMeetingResponse addMeeting(AddMeetingRequest addMeetingRequest, String username, Long gatheringId, MultipartFile file) throws IOException {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(() -> new NotFoundGatheringException("no exist Gathering!!"));
            Image image = null;
            image = saveImage(image,file);
            Meeting meeting = Meeting.builder()
                    .title(addMeetingRequest.getTitle())
                    .content(addMeetingRequest.getContent())
                    .createdBy(user)
                    .boardDate(LocalDateTime.now())
                    .startDate(addMeetingRequest.getStartDate())
                    .endDate(addMeetingRequest.getEndDate())
                    .gathering(gathering)
                    .count(1)
                    .image(image)
                    .build();
        Attend attend = Attend.builder()
                .meeting(meeting)
                .date(LocalDateTime.now())
                .accepted(true)
                .attendBy(user).build();
        if(image!=null) imageRepository.save(image);
        meetingRepository.save(meeting);
        attendRepository.save(attend);
        return AddMeetingResponse.builder()
                    .code(SUCCESS_CODE)
                    .message(SUCCESS_MESSAGE)
                    .build();
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
            return DeleteMeetingResponse.builder()
                    .code(SUCCESS_CODE)
                    .message(SUCCESS_MESSAGE)
                    .build();
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
            meeting.setTitle(updateMeetingRequest.getTitle());
            meeting.setContent(updateMeetingRequest.getContent());
            meeting.setStartDate(updateMeetingRequest.getStartDate());
            meeting.setEndDate(updateMeetingRequest.getEndDate());
            meeting.setBoardDate(LocalDateTime.now());
            meeting.setImage(image);
            return UpdateMeetingResponse.builder()
                    .code(SUCCESS_CODE)
                    .message(SUCCESS_MESSAGE)
                    .build();
    }
    public MeetingResponse meetingDetail(Long meetingId, String username) {

        userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
        List<MeetingDetailQuery> meetingDetailQueries = meetingRepository.meetingDetail(meetingId);
        if(meetingDetailQueries.size() == 0){
            throw new NotFoundMeetingExeption("no exist Meeting!!");
        }
        return MeetingResponse.builder()
                .code("SU")
                .message("Success")
                .id(meetingDetailQueries.getLast().getId())
                .title(meetingDetailQueries.getFirst().getTitle())
                .content(meetingDetailQueries.getFirst().getContent())
                .boardDate(meetingDetailQueries.getFirst().getBoardDate())
                .startDate(meetingDetailQueries.getFirst().getStartDate())
                .endDate(meetingDetailQueries.getFirst().getEndDate())
                .createdBy(meetingDetailQueries.getFirst().getCreatedBy())
                .attendedBy(attendedBy(meetingDetailQueries))
                .url(meetingDetailQueries.getFirst().getUrl())
                .build();
    }

    public MeetingsResponse meetings(int pageNum, int pageSize, String username, @RequestParam(defaultValue = "") String title) {

            userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, Sort.Direction.ASC,"id");
            Page<MeetingsQuery> page = meetingRepository.meetings(pageRequest,title);
            return MeetingsResponse.builder()
                    .code(SUCCESS_CODE)
                    .message(SUCCESS_MESSAGE)
                    .page(page)
                    .build();
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
}
