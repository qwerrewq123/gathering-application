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
import spring.myproject.domain.enrollment.dto.response.DisEnrollGatheringResponse;
import spring.myproject.domain.enrollment.dto.response.EnrollGatheringResponse;
import spring.myproject.domain.enrollment.exception.AlreadyEnrollmentException;
import spring.myproject.domain.enrollment.exception.NotFoundEnrollmentException;
import spring.myproject.domain.gathering.exception.NotFoundGatheringException;
import spring.myproject.domain.user.exception.NotFoundUserException;

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
            gathering.changeCount(gathering.getCount()-11);
            enrollmentRepository.delete(enrollment);
            return DisEnrollGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
}
