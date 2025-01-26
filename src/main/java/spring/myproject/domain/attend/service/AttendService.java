package spring.myproject.domain.attend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.domain.attend.Attend;
import spring.myproject.domain.attend.repository.AttendRepository;
import spring.myproject.domain.meeting.Meeting;
import spring.myproject.domain.meeting.repository.MeetingRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendService {

    private final UserRepository userRepository;
    private final AttendRepository attendRepository;
    private final MeetingRepository meetingRepository;

    public void addAttend(Long meetingId, String username) {
        User user = userRepository.findByUsername(username);

        if(user == null){
            throw new IllegalArgumentException("해당하는 유저가 없습니다");
        }

        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> {
            throw new IllegalArgumentException("해당하는 미팅이 없습니다");
        });

        if(meeting.getCreatedBy().getId()  == meetingId){
            throw new IllegalArgumentException("미팅 개최자는 자동으로 참여됩니다");
        }

        Attend attend = Attend.builder()
                .accepted(false)
                .attendBy(user)
                .date(LocalDateTime.now())
                .meeting(meeting)
                .build();

        attendRepository.save(attend);


    }

    public void disAttend(Long meetingId, Long attendId, String username) {

        User user = userRepository.findByUsername(username);

        if(user == null){
            throw new IllegalArgumentException("해당하는 유저가 없습니다");
        }

        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> {
            throw new IllegalArgumentException("해당하는 미팅이 없습니다");
        });

        Attend attend = attendRepository.findById(attendId).orElseThrow(() -> {
            throw new IllegalArgumentException("해당 미팅에 참석한 적이 없습니다");
        });

        if(meeting.getCreatedBy().getId()  == meetingId){
            throw new IllegalArgumentException("미팅 개최자는 탈퇴할 수 없습니다");
        }

        attendRepository.delete(attend);



    }

    public void permitAttend(Long meetingId, Long attendId, String username) {
        User user = userRepository.findByUsername(username);

        if(user == null){
            throw new IllegalArgumentException("해당하는 유저가 없습니다");
        }

        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> {
            throw new IllegalArgumentException("해당하는 미팅이 없습니다");
        });

        Attend attend = attendRepository.findById(attendId).orElseThrow(() -> {
            throw new IllegalArgumentException("해당 미팅에 참석한 적이 없습니다");
        });

        if(attend.getAccepted() == true){
            throw new IllegalArgumentException("해당 유저는 이미 미팅에 참석한 상태입니다");
        }

        if(meeting.getCreatedBy().getId()  == meetingId){
            throw new IllegalArgumentException("미팅 개최자는 항상 미팅에 참여합니다");

        }

        attend.setAccepted(true);


    }
}
