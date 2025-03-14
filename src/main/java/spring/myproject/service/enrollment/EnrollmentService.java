package spring.myproject.service.enrollment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.domain.Enrollment;
import spring.myproject.repository.enrollment.EnrollmentRepository;
import spring.myproject.domain.Gathering;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.domain.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.dto.response.enrollment.DisEnrollGatheringResponse;
import spring.myproject.dto.response.enrollment.EnrollGatheringResponse;
import spring.myproject.exception.enrollment.AlreadyEnrollmentException;
import spring.myproject.exception.enrollment.NotFoundEnrollmentException;
import spring.myproject.exception.gathering.NotFoundGatheringException;
import spring.myproject.exception.user.NotFoundUserException;

import java.time.LocalDateTime;

import static spring.myproject.util.ConstClass.*;

@Service
@Transactional
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final GatheringRepository gatheringRepository;

    public EnrollGatheringResponse enrollGathering(Long gatheringId, String username) {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(
                    () -> new NotFoundGatheringException("no exist Gathering!!"));
            Enrollment exist = enrollmentRepository.existEnrollment(gatheringId, user.getId());
            if(exist != null) throw new AlreadyEnrollmentException("Already enrolled;");
            gathering.changeCount(gathering.getCount()+1);
            Enrollment enrollment = Enrollment.of(true,gathering,user,LocalDateTime.now());
            enrollmentRepository.save(enrollment);
            return EnrollGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
    public DisEnrollGatheringResponse disEnrollGathering(Long gatheringId, String username) {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(
                () -> new NotFoundGatheringException("no exist Gathering!!"));
            Enrollment enrollment = enrollmentRepository.findEnrollment(gatheringId, user.getId()).orElseThrow(
                    () ->  new NotFoundEnrollmentException("no exist Enrollment!!"));
            gathering.changeCount(gathering.getCount()-1);
            enrollmentRepository.delete(enrollment);
            return DisEnrollGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
}
