package spring.myproject.controller.image;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.myproject.dto.response.image.GatheringImageResponse;
import spring.myproject.service.image.ImageService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    @GetMapping("/image/{imageUrl}")
    public Resource fetchImage(@PathVariable String imageUrl, HttpServletResponse response) throws IOException {
        return imageService.fetchImage(imageUrl,response);
    }

    @GetMapping("/gathering/image/{gatheringId}")
    public ResponseEntity<GatheringImageResponse> gatheringImage(@PathVariable Long gatheringId, @RequestParam Integer pageNum){
        GatheringImageResponse gatheringImageResponse =imageService.gatheringImage(gatheringId,pageNum);
        return new ResponseEntity<>(gatheringImageResponse, HttpStatus.OK);
    }
}
