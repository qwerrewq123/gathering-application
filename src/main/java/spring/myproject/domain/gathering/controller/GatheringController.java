package spring.myproject.domain.gathering.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.domain.gathering.dto.response.*;
import spring.myproject.domain.gathering.service.GatheringService;
import spring.myproject.domain.gathering.dto.request.AddGatheringRequest;
import spring.myproject.domain.gathering.dto.request.UpdateGatheringRequest;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class GatheringController {

    private final GatheringService gatheringService;

    @PostMapping("/gathering")
    public ResponseEntity<AddGatheringResponse> addGathering(@RequestPart AddGatheringRequest addGatheringRequest,
                                               @RequestPart(required = false) MultipartFile file,
                                               @AuthenticationPrincipal String username) throws IOException {

        AddGatheringResponse addGatheringResponse = gatheringService.addGathering(addGatheringRequest, file, username);
        return new ResponseEntity<>(addGatheringResponse, HttpStatus.OK);
    }

    @PutMapping("/gathering/{gatheringId}")
    public ResponseEntity<UpdateGatheringResponse> updateGathering(@RequestPart UpdateGatheringRequest updateGatheringRequest,
                                               @PathVariable Long gatheringId,
                                               @RequestPart(required = false) MultipartFile file,
                                               @AuthenticationPrincipal String username) throws IOException {

        UpdateGatheringResponse updateGatheringResponse = gatheringService.updateGathering(updateGatheringRequest, file, username, gatheringId);
        return new ResponseEntity<>(updateGatheringResponse, HttpStatus.OK);
    }

    @GetMapping("/gathering/{gatheringId}")
    public ResponseEntity<GatheringResponse> gatheringDetail(@PathVariable Long gatheringId, @AuthenticationPrincipal String username) throws IOException {

        GatheringResponse gatheringResponse = gatheringService.gatheringDetail(gatheringId,username);
        return new ResponseEntity<>(gatheringResponse, HttpStatus.OK);
    }

    @GetMapping("/gatherings")
    public ResponseEntity<TotalGatheringsResponse> gatherings(@RequestParam String title,
                                             @AuthenticationPrincipal String username){

        TotalGatheringsResponse totalGatheringsResponse = gatheringService.gatherings(username, title);
        return new ResponseEntity<>(totalGatheringsResponse, HttpStatus.OK);
    }
    @GetMapping("/gathering")
    public ResponseEntity<GatheringPagingResponse> gatheringCategory(@RequestParam String category,
                                                               @RequestParam Integer pageNum,
                                                               @RequestParam Integer pageSize,
                                                               @AuthenticationPrincipal String username
                                                               ){

        GatheringPagingResponse gatheringPagingResponse = gatheringService.gatheringCategory(category,pageNum,pageSize,username);
        return new ResponseEntity<>(gatheringPagingResponse, HttpStatus.OK);
    }


    @GetMapping("/gatherings/like")
    public ResponseEntity<GatheringPagingResponse> gatheringsLike(@RequestParam int pageNum,
                                                                  @RequestParam Integer pageSize,
                                                 @AuthenticationPrincipal String username){

        GatheringPagingResponse gatheringPagingResponse = gatheringService.gatheringsLike(pageNum,pageSize,username);
        return new ResponseEntity<>(gatheringPagingResponse, HttpStatus.OK);
    }
}
