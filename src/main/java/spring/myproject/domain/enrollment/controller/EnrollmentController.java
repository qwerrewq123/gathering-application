package spring.myproject.domain.enrollment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.myproject.domain.enrollment.service.EnrollmentService;
import spring.myproject.dto.response.enrollment.DisEnrollGatheringResponse;
import spring.myproject.dto.response.enrollment.EnrollGatheringResponse;

@RestController
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    @PostMapping("/gathering/{gatheringId}/participate")
    public ResponseEntity<Object> enrollGathering(@RequestParam Long gatheringId,
                                                       @AuthenticationPrincipal String username){
        try {
            enrollmentService.enrollGathering(gatheringId,username);
            return new ResponseEntity<>(EnrollGatheringResponse.builder()
                    .code("SU")
                    .message("Success"), HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }

    @PostMapping("/gathering/{gatheringId}/disParticipate")
    public ResponseEntity<Object> disEnrollGathering(@RequestParam Long gatheringId,
                                                           @AuthenticationPrincipal String username){
        try {
            enrollmentService.disEnrollGathering(gatheringId,username);
            return new ResponseEntity<>(DisEnrollGatheringResponse.builder()
                    .code("SU")
                    .message("Success"),HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }
}
