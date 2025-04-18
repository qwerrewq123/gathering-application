package spring.myproject.controller.gathering;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.common.annotation.Id;
import spring.myproject.dto.response.recommend.RecommendResponse;
import spring.myproject.service.gathering.GatheringService;
import spring.myproject.service.recommend.RecommendService;

import java.io.IOException;
import java.time.LocalDate;

import static spring.myproject.dto.request.gathering.GatheringRequestDto.*;
import static spring.myproject.dto.response.gathering.GatheringResponseDto.*;

@RestController
@RequiredArgsConstructor
public class GatheringController {

    private final GatheringService gatheringService;

    @PostMapping("/gathering")
    public ResponseEntity<AddGatheringResponse> addGathering(@RequestPart AddGatheringRequest addGatheringRequest,
                                                             @RequestPart(required = false) MultipartFile file,
                                                             @Id Long userId) throws IOException {

        AddGatheringResponse addGatheringResponse = gatheringService.addGathering(addGatheringRequest, file, userId);
        return new ResponseEntity<>(addGatheringResponse, HttpStatus.OK);
    }

    @PutMapping("/gathering/{gatheringId}")
    public ResponseEntity<UpdateGatheringResponse> updateGathering(@RequestPart UpdateGatheringRequest updateGatheringRequest,
                                                                   @PathVariable Long gatheringId,
                                                                   @RequestPart(required = false) MultipartFile file,
                                                                   @Id Long userId) throws IOException {

        UpdateGatheringResponse updateGatheringResponse = gatheringService.updateGathering(updateGatheringRequest, file, userId, gatheringId);
        return new ResponseEntity<>(updateGatheringResponse, HttpStatus.OK);
    }

    @GetMapping("/gathering/{gatheringId}")
    public ResponseEntity<GatheringResponse> gatheringDetail(@PathVariable Long gatheringId, @Id Long userId) throws IOException {

        GatheringResponse gatheringResponse = gatheringService.gatheringDetail(gatheringId,userId);
        return new ResponseEntity<>(gatheringResponse, HttpStatus.OK);
    }

    @GetMapping("/gatherings")
    public ResponseEntity<MainGatheringResponse> gatherings(@RequestParam(defaultValue = "") String title,
                                                            @Id Long userId){

        MainGatheringResponse mainGatheringResponse = gatheringService.gatherings(userId, title);
        return new ResponseEntity<>(mainGatheringResponse, HttpStatus.OK);
    }
    @GetMapping("/gathering")
    public ResponseEntity<GatheringCategoryResponse> gatheringCategory(@RequestParam String category,
                                                                     @RequestParam Integer pageNum,
                                                                     @RequestParam Integer pageSize, @Id Long userId
                                                               ){
        GatheringCategoryResponse gatheringCategoryResponse = gatheringService.gatheringCategory(category,pageNum,pageSize,userId);
        return new ResponseEntity<>(gatheringCategoryResponse, HttpStatus.OK);
    }


    @GetMapping("/gatherings/like")
    public ResponseEntity<GatheringLikeResponse> gatheringsLike(@RequestParam int pageNum,
                                                                  @RequestParam Integer pageSize,
                                                                @Id Long userId){
        GatheringLikeResponse gatheringLikeResponse = gatheringService.gatheringsLike(pageNum,pageSize,userId);
        return new ResponseEntity<>(gatheringLikeResponse, HttpStatus.OK);
    }


}
