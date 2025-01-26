package spring.myproject.domain.like.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import spring.myproject.domain.like.service.LikeService;
import spring.myproject.dto.response.like.DislikeResponse;
import spring.myproject.dto.response.like.LikeResponse;


@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PatchMapping("/like/{gatheringId}")
    public ResponseEntity<Object> like(@PathVariable Long gatheringId, @AuthenticationPrincipal String username){

        try {
            likeService.like(gatheringId,username);
            LikeResponse likeResponse = LikeResponse.builder()
                    .code("SU")
                    .message("Success")
                    .build();

            return new ResponseEntity<>(likeResponse, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }


    @PatchMapping("/dislike/{gatheringId}")
    public ResponseEntity<Object> dislike(@PathVariable Long gatheringId, @AuthenticationPrincipal String username){
        try {
            likeService.dislike(gatheringId,username);
            DislikeResponse dislikeResponse = DislikeResponse.builder()
                    .code("SU")
                    .message("Success")
                    .build();

            return new ResponseEntity<>(dislikeResponse, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }


}
