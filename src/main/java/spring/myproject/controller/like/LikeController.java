package spring.myproject.controller.like;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import spring.myproject.common.Username;
import spring.myproject.dto.response.like.LikeResponseDto;
import spring.myproject.service.like.LikeService;

import static spring.myproject.dto.response.like.LikeResponseDto.*;


@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PatchMapping("/gathering/{gatheringId}/like")
    public ResponseEntity<LikeResponse> like(@PathVariable Long gatheringId, @Username String username){

        LikeResponse likeResponse = likeService.like(gatheringId, username);
        return new ResponseEntity<>(likeResponse, HttpStatus.OK);
    }


    @PatchMapping("/gathering/{gatheringId}/dislike")
    public ResponseEntity<DislikeResponse> dislike(@PathVariable Long gatheringId, @Username String username){

        DislikeResponse dislikeResponse = likeService.dislike(gatheringId, username);
        return new ResponseEntity<>(dislikeResponse, HttpStatus.OK);
    }
}
