package spring.myproject.domain.gathering.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.domain.gathering.service.GatheringService;
import spring.myproject.dto.request.gathering.AddGatheringRequest;
import spring.myproject.dto.request.gathering.UpdateGatheringRequest;
import spring.myproject.dto.response.gathering.AddGatheringResponse;
import spring.myproject.dto.response.gathering.GatheringPagingResponse;
import spring.myproject.dto.response.gathering.GatheringResponse;
import spring.myproject.dto.response.gathering.UpdateGatheringResponse;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class GatheringController {

    private final GatheringService gatheringService;

    @PostMapping("/gathering")
    public ResponseEntity<Object> addGathering(@RequestPart AddGatheringRequest addGatheringRequest,
                                               @RequestPart MultipartFile file,
                                               @AuthenticationPrincipal String username) throws IOException {
        try {
            System.out.println(username);
            gatheringService.addGathering(addGatheringRequest,file,username);
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

    @PutMapping("/gathering/{gatheringId}")
    public ResponseEntity<Object> updateGathering(@RequestPart UpdateGatheringRequest updateGatheringRequest,
                                               @PathVariable Long gatheringId,
                                               @RequestPart MultipartFile file,
                                               @AuthenticationPrincipal String username) throws IOException {
        try {
            gatheringService.updateGathering(updateGatheringRequest,file,username,gatheringId);
            UpdateGatheringResponse updateGatheringResponse = UpdateGatheringResponse.builder()
                    .code("SU")
                    .message("Success")
                    .build();
            return new ResponseEntity<>(updateGatheringResponse, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }




    @GetMapping("/gathering/{gatheringId}")
    public ResponseEntity<Object> gatheringDetail(@PathVariable Long gatheringId, @AuthenticationPrincipal String username) throws IOException {
        try {
            GatheringResponse gatheringResponse = gatheringService.gatheringDetail(gatheringId,username);
            return new ResponseEntity<>(gatheringResponse,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            throw e ;
        }
    }

    @GetMapping("/gatherings")
    public ResponseEntity<Object> gatherings(@RequestParam int pageNum,
                                             @RequestParam String title,
                                             @AuthenticationPrincipal String username){
        try {
            Page<GatheringPagingResponse> page = gatheringService.gatherings(pageNum,username,title);
            return new ResponseEntity<>(new PagedModel<>(page),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            throw e ;
        }
    }

    @GetMapping("/gatherings/like")
    public ResponseEntity<Object> gatheringsLike(@RequestParam int pageNum,
                                                 @AuthenticationPrincipal String username){
        try {

            Page<GatheringPagingResponse> page = gatheringService.gatheringsLike(pageNum,username);
            return new ResponseEntity<>(new PagedModel<>(page),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }






}
