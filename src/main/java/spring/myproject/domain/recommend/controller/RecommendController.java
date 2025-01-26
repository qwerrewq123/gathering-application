package spring.myproject.domain.recommend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.myproject.domain.gathering.service.GatheringService;
import spring.myproject.domain.recommend.repository.RecommendRepository;
import spring.myproject.dto.response.gathering.GatheringResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecommendController {

    private final GatheringService gatheringService;

    @GetMapping("/recommend")
    public ResponseEntity<Object> recommend(@AuthenticationPrincipal String username){

        try {

            List<GatheringResponse> gatheringResponseList = gatheringService.recommend(username);
            return new ResponseEntity<>(gatheringResponseList, HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }
}
