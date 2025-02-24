package spring.myproject.domain.like.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import spring.myproject.domain.like.service.LikeService;
import spring.myproject.domain.like.dto.response.DislikeResponse;
import spring.myproject.domain.like.dto.response.LikeResponse;


@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PatchMapping("/like/{gatheringId}")
    public ResponseEntity<LikeResponse> like(@PathVariable Long gatheringId, @AuthenticationPrincipal String username){

        LikeResponse likeResponse = likeService.like(gatheringId, username);
        return new ResponseEntity<>(likeResponse, HttpStatus.OK);
    }


    @PatchMapping("/dislike/{gatheringId}")
    public ResponseEntity<DislikeResponse> dislike(@PathVariable Long gatheringId, @AuthenticationPrincipal String username){

        DislikeResponse dislikeResponse = likeService.dislike(gatheringId, username);
        return new ResponseEntity<>(dislikeResponse, HttpStatus.OK);
    }
}
