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
import spring.myproject.dto.response.meeting.MeetingListResponse;
import spring.myproject.dto.response.meeting.MeetingQueryListResponse;
import spring.myproject.dto.response.meeting.MeetingQueryResponse;
import spring.myproject.dto.response.meeting.MeetingResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final GatheringRepository gatheringRepository;

    public void addMeeting(AddMeetingRequest addMeetingRequest, String username,Long gatheringId) {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw  new IllegalArgumentException("해당되는 유저가 존재하지 않습니다");
        }
        Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(() -> {
            throw new IllegalArgumentException("해당하는 소모임이 없습니다");
        });
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

    }

    public void deleteMeeting(String username, Long meetingId) {

        User user = userRepository.findByUsername(username);
        if(user == null){
            throw  new IllegalArgumentException("해당되는 유저가 존재하지 않습니다");
        }

        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> {
            throw new IllegalArgumentException("해당하는 미팅이 없습니다");
        });

        boolean authorize = meeting.getCreatedBy().getId() == user.getId();
        if(authorize == false){
            throw new IllegalArgumentException("해당하는 권한이 없습니다");
        }

        meetingRepository.delete(meeting);

    }


    public void updateMeeting(UpdateMeetingRequest updateMeetingRequest, String username, Long meetingId) {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw  new IllegalArgumentException("해당되는 유저가 존재하지 않습니다");
        }

        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> {
            throw new IllegalArgumentException("해당하는 미팅이 없습니다");
        });

        boolean authorize = meeting.getCreatedBy().getId() == user.getId();
        if(authorize == false){
            throw new IllegalArgumentException("해당하는 권한이 없습니다");
        }

        meeting.setTitle(updateMeetingRequest.getTitle());
        meeting.setContent(updateMeetingRequest.getContent());
        meeting.setStartDate(updateMeetingRequest.getStartDate());
        meeting.setEndDate(updateMeetingRequest.getEndDate());
    }

    public MeetingResponse meetingDetail(Long meetingId, String username) {

        User user = userRepository.findByUsername(username);
        if(user == null){
            throw  new IllegalArgumentException("해당되는 유저가 존재하지 않습니다");
        }

        List<MeetingQueryResponse> meetingQueryResponses = meetingRepository.findAttendsBy(meetingId);
        if(meetingQueryResponses.size() == 0){
            throw new IllegalArgumentException("해당하는 미팅이 존재하지가 않습니다");
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

    public Page<MeetingListResponse> meetings(int pageNum, String username,String title) {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw  new IllegalArgumentException("해당되는 유저가 존재하지 않습니다");
        }
        PageRequest pageRequest = PageRequest.of(pageNum - 1, 10, Sort.Direction.ASC,"id");
        Page<MeetingQueryListResponse> meetings = meetingRepository.meetings(pageRequest,title);
        return meetings.map(m -> MeetingListResponse.builder()
                .code("SU")
                .message("Success")
                .title(m.getTitle())
                .content(m.getContent())
                .boardDate(m.getBoardDate())
                .startDate(m.getStartDate())
                .endDate(m.getEndDate())
                .createdBy(m.getCreatedBy())
                .content(m.getContent())
                .build());



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
