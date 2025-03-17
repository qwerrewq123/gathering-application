package spring.myproject.controller.gathering;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.common.Username;
import spring.myproject.dto.response.gathering.*;
import spring.myproject.dto.response.recommend.RecommendResponse;
import spring.myproject.service.gathering.GatheringService;
import spring.myproject.dto.request.gathering.AddGatheringRequest;
import spring.myproject.dto.request.gathering.UpdateGatheringRequest;
import spring.myproject.service.recommend.RecommendService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class GatheringController {

    private final GatheringService gatheringService;
    private final RecommendService recommendService;

    @PostMapping("/gathering")
    public ResponseEntity<AddGatheringResponse> addGathering(@RequestPart AddGatheringRequest addGatheringRequest,
                                                             @RequestPart(required = false) MultipartFile file,
                                                             @Username String username) throws IOException {

        AddGatheringResponse addGatheringResponse = gatheringService.addGathering(addGatheringRequest, file, username);
        return new ResponseEntity<>(addGatheringResponse, HttpStatus.OK);
    }

    @PutMapping("/gathering/{gatheringId}")
    public ResponseEntity<UpdateGatheringResponse> updateGathering(@RequestPart UpdateGatheringRequest updateGatheringRequest,
                                                                   @PathVariable Long gatheringId,
                                                                   @RequestPart(required = false) MultipartFile file,
                                                                   @Username String username) throws IOException {

        UpdateGatheringResponse updateGatheringResponse = gatheringService.updateGathering(updateGatheringRequest, file, username, gatheringId);
        return new ResponseEntity<>(updateGatheringResponse, HttpStatus.OK);
    }

    @GetMapping("/gathering/{gatheringId}")
    public ResponseEntity<GatheringResponse> gatheringDetail(@PathVariable Long gatheringId, @Username String username) throws IOException {

        GatheringResponse gatheringResponse = gatheringService.gatheringDetail(gatheringId,username);
        return new ResponseEntity<>(gatheringResponse, HttpStatus.OK);
    }

    @GetMapping("/gatherings")
    public ResponseEntity<TotalGatheringsResponse> gatherings(@RequestParam(defaultValue = "") String title,
                                                              @Username String username){

        TotalGatheringsResponse totalGatheringsResponse = gatheringService.gatherings(username, title);
        return new ResponseEntity<>(totalGatheringsResponse, HttpStatus.OK);
    }
    @GetMapping("/gathering")
    public ResponseEntity<GatheringPagingResponse> gatheringCategory(@RequestParam String category,
                                                                     @RequestParam Integer pageNum,
                                                                     @RequestParam Integer pageSize,
                                                                     @Username String username
                                                               ){

        GatheringPagingResponse gatheringPagingResponse = gatheringService.gatheringCategory(category,pageNum,pageSize,username);
        return new ResponseEntity<>(gatheringPagingResponse, HttpStatus.OK);
    }


    @GetMapping("/gatherings/like")
    public ResponseEntity<GatheringPagingResponse> gatheringsLike(@RequestParam int pageNum,
                                                                  @RequestParam Integer pageSize,
                                                 @Username String username){

        GatheringPagingResponse gatheringPagingResponse = gatheringService.gatheringsLike(pageNum,pageSize,username);
        return new ResponseEntity<>(gatheringPagingResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RecommendResponse> fetchRecommendTop10(){
        RecommendResponse recommendResponse = recommendService.fetchRecommendTop10(LocalDate.now());
        return new ResponseEntity<>(recommendResponse, HttpStatus.OK);
    }
}
