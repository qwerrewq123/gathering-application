package spring.myproject.controller.enrollment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.myproject.common.annotation.Id;
import spring.myproject.service.enrollment.EnrollmentService;

import static spring.myproject.dto.response.enrollment.EnrollResponseDto.*;

@RestController
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    @PostMapping("/gathering/{gatheringId}/participate")
    public ResponseEntity<EnrollGatheringResponse> enrollGathering(@PathVariable Long gatheringId,
                                                                   @Id Long userId){

        EnrollGatheringResponse enrollGatheringResponse = enrollmentService.enrollGathering(gatheringId, userId);
        return new ResponseEntity<>(enrollGatheringResponse, HttpStatus.OK);
    }

    @PostMapping("/gathering/{gatheringId}/disParticipate")
    public ResponseEntity<DisEnrollGatheringResponse> disEnrollGathering(@PathVariable Long gatheringId,
                                                                         @Id Long userId){

        DisEnrollGatheringResponse disEnrollGatheringResponse = enrollmentService.disEnrollGathering(gatheringId, userId);
        return new ResponseEntity<>(disEnrollGatheringResponse, HttpStatus.OK);
    }
}
