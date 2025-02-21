package spring.myproject.domain.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import spring.myproject.domain.attend.repository.AttendRepository;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.gathering.repository.GatheringRepository;
import spring.myproject.domain.meeting.Meeting;
import spring.myproject.domain.meeting.repository.MeetingRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;
import spring.myproject.dto.request.meeting.AddMeetingRequest;
import spring.myproject.dto.request.meeting.UpdateMeetingRequest;
import spring.myproject.dto.response.enrollment.EnrollGatheringResponse;
import spring.myproject.dto.response.meeting.*;
import spring.myproject.exception.gathering.NotFoundGatheringException;
import spring.myproject.exception.meeting.NotAuthrizeException;
import spring.myproject.exception.meeting.NotFoundMeeting;
import spring.myproject.exception.user.NotFoundUserException;
import spring.myproject.util.GatheringConst;
import spring.myproject.util.MeetingConst;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static spring.myproject.util.UserConst.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final GatheringRepository gatheringRepository;

    public AddMeetingResponse addMeeting(AddMeetingRequest addMeetingRequest, String username,Long gatheringId) {

        try {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));

            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(() -> new NotFoundGatheringException("no exist Gathering!!"));

            Meeting meeting = Meeting.builder()
                    .title(addMeetingRequest.getTitle())
                    .content(addMeetingRequest.getContent())
                    .createdBy(user)
                    .boardDate(LocalDateTime.now())
                    .startDate(addMeetingRequest.getStartDate())
                    .endDate(addMeetingRequest.getEndDate())
                    .gathering(gathering)
                    .build();

            meetingRepository.save(meeting);
            return AddMeetingResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();


        }catch (NotFoundUserException e){
            return AddMeetingResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();

        }catch (NotFoundGatheringException e){
            return AddMeetingResponse.builder()
                    .code(GatheringConst.notFoundGatheringCode)
                    .message(GatheringConst.notFoundGatheringMessage)
                    .build();

        }catch (Exception e){
            return AddMeetingResponse.builder()
                    .code(dbErrorCode)
                    .message(dbErrorMessage)
                    .build();
        }

    }

    public DeleteMeetingResponse deleteMeeting(String username, Long meetingId) {


        try {
            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeeting("no exist Meeting!!"));

            boolean authorize = meeting.getCreatedBy().getId() == user.getId();
            if(authorize == false){
                throw new NotAuthrizeException("no authority!");
            }
            meetingRepository.delete(meeting);
            return DeleteMeetingResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();

        }catch (NotFoundUserException e){
            return DeleteMeetingResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();

        }catch (NotFoundMeeting e){
            return DeleteMeetingResponse.builder()
                    .code(MeetingConst.notFoundCode)
                    .message(MeetingConst.notFoundMessage)
                    .build();
        }catch (NotAuthrizeException e){
            return DeleteMeetingResponse.builder()
                    .code(MeetingConst.notAuthorizeCode)
                    .message(MeetingConst.notAuthorizedMessage)
                    .build();
        }

    }


    public UpdateMeetingResponse updateMeeting(UpdateMeetingRequest updateMeetingRequest, String username, Long meetingId) {


        try {
            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));

            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeeting("no exist Meeting!!"));

            boolean authorize = meeting.getCreatedBy().getId() == user.getId();
            if(authorize == false){
                throw new IllegalArgumentException("no authority!");
            }

            meeting.setTitle(updateMeetingRequest.getTitle());
            meeting.setContent(updateMeetingRequest.getContent());
            meeting.setStartDate(updateMeetingRequest.getStartDate());
            meeting.setEndDate(updateMeetingRequest.getEndDate());

            return UpdateMeetingResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();

        }catch (NotFoundUserException e){
            return UpdateMeetingResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();

        }catch (NotFoundMeeting e){
            return UpdateMeetingResponse.builder()
                    .code(MeetingConst.notFoundCode)
                    .message(MeetingConst.notFoundMessage)
                    .build();
        }catch (NotAuthrizeException e){
            return UpdateMeetingResponse.builder()
                    .code(MeetingConst.notAuthorizeCode)
                    .message(MeetingConst.notAuthorizedMessage)
                    .build();
        }
    }

    public MeetingResponse meetingDetail(Long meetingId, String username) {

        User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));

        List<MeetingQueryResponse> meetingQueryResponses = meetingRepository.findAttendsBy(meetingId);
        if(meetingQueryResponses.size() == 0){
            throw new NotFoundMeeting("no exist Meeting!!");
        }


        return MeetingResponse.builder()
                .code("SU")
                .message("Success")
                .id(meetingQueryResponses.getLast().getId())
                .title(meetingQueryResponses.getFirst().getTitle())
                .content(meetingQueryResponses.getFirst().getContent())
                .boardDate(meetingQueryResponses.getFirst().getBoardDate())
                .startDate(meetingQueryResponses.getFirst().getStartDate())
                .endDate(meetingQueryResponses.getFirst().getEndDate())
                .createdBy(meetingQueryResponses.getFirst().getCreatedBy())
                .attendedBy(attendedBy(meetingQueryResponses))
                .build();



    }

    public MeetingListResponse meetings(int pageNum, String username,String title) {


        try {
            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            PageRequest pageRequest = PageRequest.of(pageNum - 1, 10, Sort.Direction.ASC,"id");
            Page<MeetingQueryListResponse> page = meetingRepository.meetings(pageRequest,title);
            return MeetingListResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .page(page)
                    .build();

        }catch (NotFoundUserException e){
            return MeetingListResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();

        }catch (Exception e){
            return MeetingListResponse.builder()
                    .code(dbErrorCode)
                    .message(dbErrorMessage)
                    .build();
        }

    }



    private List<String> attendedBy(List<MeetingQueryResponse> meetingQueryResponses){

        List<String> attendedBy = new ArrayList<>();

        for (MeetingQueryResponse meetingQueryResponse : meetingQueryResponses) {
            if(StringUtils.hasText(meetingQueryResponse.getAttendedBy())){
                attendedBy.add(meetingQueryResponse.getAttendedBy());
            }
        }

        return attendedBy;
    }

}
