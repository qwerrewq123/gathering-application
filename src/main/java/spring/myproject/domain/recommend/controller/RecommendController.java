package spring.myproject.domain.recommend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.myproject.domain.recommend.service.RecommendService;
import spring.myproject.domain.recommend.dto.response.RecommendResponse;

@RestController
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/recommend")
    public ResponseEntity<Object> recommend(@AuthenticationPrincipal String username){


        RecommendResponse recommendResponse = recommendService.recommend(username);
        if(recommendResponse.getCode().equals("SU")){
            return new ResponseEntity<>(recommendResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(recommendResponse, HttpStatus.BAD_REQUEST);
        }


    }
}
