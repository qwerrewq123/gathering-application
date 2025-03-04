package spring.myproject.domain.image.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import spring.myproject.domain.image.service.ImageService;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    @GetMapping("/image/{imageUrl}")
    public ResponseEntity<Resource> image(@PathVariable String imageUrl) throws IOException {
        Resource resource = imageService.image(imageUrl);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    //TODO : 하나의 소모임과 관련된 모든 이미지 불러오기
}
