package spring.myproject.domain.enrollment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.myproject.annotation.Username;
import spring.myproject.domain.enrollment.service.EnrollmentService;
import spring.myproject.dto.response.enrollment.DisEnrollGatheringResponse;
import spring.myproject.dto.response.enrollment.EnrollGatheringResponse;

@RestController
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    @PostMapping("/gathering/{gatheringId}/participate")
    public ResponseEntity<EnrollGatheringResponse> enrollGathering(@PathVariable Long gatheringId,
                                                       @Username String username){
        EnrollGatheringResponse enrollGatheringResponse = enrollmentService.enrollGathering(gatheringId, username);
        if(enrollGatheringResponse.getCode().equals("SU")){
            return new ResponseEntity<>(enrollGatheringResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(enrollGatheringResponse, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/gathering/{gatheringId}/disParticipate")
    public ResponseEntity<DisEnrollGatheringResponse> disEnrollGathering(@PathVariable Long gatheringId,
                                                           @Username String username){
        DisEnrollGatheringResponse disEnrollGatheringResponse = enrollmentService.disEnrollGathering(gatheringId, username);
        if(disEnrollGatheringResponse.getCode().equals("SU")){
            return new ResponseEntity<>(disEnrollGatheringResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(disEnrollGatheringResponse, HttpStatus.BAD_REQUEST);
        }

    }
}
