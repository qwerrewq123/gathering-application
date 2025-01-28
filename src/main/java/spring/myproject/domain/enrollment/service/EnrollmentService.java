package spring.myproject.domain.enrollment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.domain.enrollment.Enrollment;
import spring.myproject.domain.enrollment.repository.EnrollmentRepository;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.gathering.repository.GatheringRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final GatheringRepository gatheringRepository;


    public void enrollGathering(Long gatheringId, String username) {

        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("해당하는 유저가 없습니다");
        }

        Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(() -> {
            throw new IllegalArgumentException("해당하는 소모임이 없습니다");
        });

        Enrollment exist = enrollmentRepository.existEnrollment(gatheringId, user.getId());
        if(exist != null){
            throw new IllegalArgumentException("이미 해당 소모임이 등록신청하였거나 가입되어있습니다");
        }

        Enrollment enrollment = Enrollment.builder()
                .date(LocalDateTime.now())
                .enrolledBy(user)
                .gathering(gathering)
                .accepted(true)
                .build();

        enrollmentRepository.save(enrollment);


    }

    public void disEnrollGathering(Long gatheringId, String username) {

        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("해당하는 유저가 없습니다");
        }

        Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(() -> {
            throw new IllegalArgumentException("해당하는 소모임이 없습니다");
        });

        Enrollment enrollment = enrollmentRepository.findEnrollment(gatheringId, user.getId());

        if(enrollment == null){
            throw new IllegalArgumentException("해당 소모임의 회원이 아닙니다");
        }

        gathering.getEnrollments().remove(enrollment);
        enrollmentRepository.delete(enrollment);



    }
}
