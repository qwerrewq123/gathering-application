package spring.myproject.controller.image;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import spring.myproject.dto.response.image.GatheringImageResponse;
import spring.myproject.service.image.ImageService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    @GetMapping("/image/{imageUrl}")
    public ResponseEntity<Resource> image(@PathVariable String imageUrl) throws IOException {
        Resource resource = imageService.image(imageUrl);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @GetMapping("/gathering/image/{gatheringId}")
    public ResponseEntity<GatheringImageResponse> gatheringImage(@PathVariable Long gatheringId){
        GatheringImageResponse gatheringImageResponse =imageService.gatheringImage(gatheringId);
        return new ResponseEntity<>(gatheringImageResponse, HttpStatus.OK);
    }
}
