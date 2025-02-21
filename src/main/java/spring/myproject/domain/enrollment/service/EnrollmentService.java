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
import spring.myproject.dto.response.enrollment.DisEnrollGatheringResponse;
import spring.myproject.dto.response.enrollment.EnrollGatheringResponse;
import spring.myproject.exception.enrollment.AlreadyEnrollmentException;
import spring.myproject.exception.enrollment.NotFoundEnrollmentException;
import spring.myproject.exception.gathering.NotFoundGatheringException;
import spring.myproject.exception.user.NotFoundUserException;
import spring.myproject.util.EnrollmentConst;
import spring.myproject.util.GatheringConst;

import java.time.LocalDateTime;

import static spring.myproject.util.UserConst.*;

@Service
@Transactional
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final GatheringRepository gatheringRepository;


    public EnrollGatheringResponse enrollGathering(Long gatheringId, String username) {


        try {
            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));


            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(() -> new NotFoundGatheringException("no exist Gathering!!"));

            Enrollment exist = enrollmentRepository.existEnrollment(gatheringId, user.getId());
            if(exist != null){
                throw new AlreadyEnrollmentException("Already enrolled;");
            }

            Enrollment enrollment = Enrollment.builder()
                    .date(LocalDateTime.now())
                    .enrolledBy(user)
                    .gathering(gathering)
                    .accepted(true)
                    .build();

            enrollmentRepository.save(enrollment);

            return EnrollGatheringResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();

        }catch (NotFoundUserException e){
            return EnrollGatheringResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();

        }catch (NotFoundGatheringException e){
            return EnrollGatheringResponse.builder()
                    .code(GatheringConst.notFoundGatheringCode)
                    .message(GatheringConst.notFoundGatheringMessage)
                    .build();

        }catch (AlreadyEnrollmentException e){
            return EnrollGatheringResponse.builder()
                    .code(EnrollmentConst.alreadyEnrollmentCode)
                    .message(EnrollmentConst.alreadyEnrollmentMessage)
                    .build();
        }


    }

    public DisEnrollGatheringResponse disEnrollGathering(Long gatheringId, String username) {



        try {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));

            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(
                    () ->  new NotFoundGatheringException("no exist Gathering!!"));

            Enrollment enrollment = enrollmentRepository.findEnrollment(gatheringId, user.getId()).orElseThrow(
                    () ->  new NotFoundEnrollmentException("no exist Enrollment!!"));


            gathering.getEnrollments().remove(enrollment);
            enrollmentRepository.delete(enrollment);
            return DisEnrollGatheringResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();

        }catch (NotFoundUserException e){
            return DisEnrollGatheringResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();

        }catch (NotFoundGatheringException e){
            return DisEnrollGatheringResponse.builder()
                    .code(GatheringConst.notFoundGatheringCode)
                    .message(GatheringConst.notFoundGatheringMessage)
                    .build();

        }catch (NotFoundEnrollmentException e){
            return DisEnrollGatheringResponse.builder()
                    .code(EnrollmentConst.notFoundEnrollmentCode)
                    .message(EnrollmentConst.notFoundEnrollmentMessage)
                    .build();
        }



    }
}
