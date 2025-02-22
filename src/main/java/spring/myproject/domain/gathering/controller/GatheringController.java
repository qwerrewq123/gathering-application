package spring.myproject.domain.gathering.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.annotation.Username;
import spring.myproject.domain.gathering.service.GatheringService;
import spring.myproject.dto.request.gathering.AddGatheringRequest;
import spring.myproject.dto.request.gathering.UpdateGatheringRequest;
import spring.myproject.dto.response.gathering.AddGatheringResponse;
import spring.myproject.dto.response.gathering.GatheringPagingResponse;
import spring.myproject.dto.response.gathering.GatheringResponse;
import spring.myproject.dto.response.gathering.UpdateGatheringResponse;
import spring.myproject.security.CustomUserDetails;

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
        if(addGatheringResponse.getCode().equals("SU")){
            return new ResponseEntity<>(addGatheringResponse, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(addGatheringResponse, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/gathering/{gatheringId}")
    public ResponseEntity<UpdateGatheringResponse> updateGathering(@RequestPart UpdateGatheringRequest updateGatheringRequest,
                                               @PathVariable Long gatheringId,
                                               @RequestPart(required = false) MultipartFile file,
                                               @AuthenticationPrincipal String username) throws IOException {
        UpdateGatheringResponse updateGatheringResponse = gatheringService.updateGathering(updateGatheringRequest, file, username, gatheringId);

        if(updateGatheringResponse.getCode().equals("SU")){
            return new ResponseEntity<>(updateGatheringResponse, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(updateGatheringResponse, HttpStatus.BAD_REQUEST);
        }
    }




    @GetMapping("/gathering/{gatheringId}")
    public ResponseEntity<GatheringResponse> gatheringDetail(@PathVariable Long gatheringId, @Username String username) {
        GatheringResponse gatheringResponse = gatheringService.gatheringDetail(gatheringId,"username");
        if(gatheringResponse.getCode().equals("SU")){
            return new ResponseEntity<>(gatheringResponse, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(gatheringResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/gatherings")
    public ResponseEntity<GatheringPagingResponse> gatherings(@RequestParam int pageNum,
                                             @RequestParam String title,
                                             @AuthenticationPrincipal String username){
        GatheringPagingResponse gatheringPagingResponse = gatheringService.gatherings(pageNum, username, title);
        if(gatheringPagingResponse.getCode().equals("SU")){
            return new ResponseEntity<>(gatheringPagingResponse, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(gatheringPagingResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/gatherings/like")
    public ResponseEntity<GatheringPagingResponse> gatheringsLike(@RequestParam int pageNum,
                                                 @AuthenticationPrincipal String username){

        GatheringPagingResponse gatheringPagingResponse = gatheringService.gatheringsLike(pageNum,username);
        if(gatheringPagingResponse.getCode().equals("SU")){
            return new ResponseEntity<>(gatheringPagingResponse, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(gatheringPagingResponse, HttpStatus.BAD_REQUEST);
        }
    }






}
