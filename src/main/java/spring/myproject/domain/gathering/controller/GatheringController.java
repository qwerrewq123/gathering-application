package spring.myproject.domain.gathering.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spring.myproject.domain.gathering.service.GatheringService;
import spring.myproject.dto.request.gathering.GatheringRequest;
import spring.myproject.dto.response.gathering.AddGatheringResponse;

@RestController
@RequiredArgsConstructor
public class GatheringController {

    private final GatheringService gatheringService;

    @PostMapping("/gathering")
    public ResponseEntity<Object> addGathering(@RequestBody GatheringRequest gatheringRequest){
        try {
            gatheringService.addGathering(gatheringRequest);
            AddGatheringResponse addGatheringResponse = AddGatheringResponse.builder()
                    .code("SU")
                    .message("Success")
                    .build();
            return new ResponseEntity<>(addGatheringResponse, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
