package spring.myproject.controller.recommend;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.myproject.common.Username;
import spring.myproject.service.recommend.RecommendService;
import spring.myproject.dto.response.recommend.RecommendResponse;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/recommend")
    public ResponseEntity<Object> recommend(@Username String username){

        RecommendResponse recommendResponse = recommendService.fetchRecommendTop10(LocalDateTime.now().toLocalDate());
        return new ResponseEntity<>(recommendResponse, HttpStatus.OK);
    }
}
