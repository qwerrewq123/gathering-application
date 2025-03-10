package spring.myproject.domain.image.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import spring.myproject.domain.image.dto.response.GatheringImageResponse;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.s3.S3ImageDownloadService;
import spring.myproject.util.ConstClass;

import java.io.IOException;
import java.util.List;

import static spring.myproject.util.ConstClass.SUCCESS_CODE;
import static spring.myproject.util.ConstClass.SUCCESS_MESSAGE;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    private final S3ImageDownloadService s3ImageDownloadService;
    private final ImageRepository imageRepository;
    public Resource image(String imageUrl) throws IOException {
        byte[] imageBytes = s3ImageDownloadService.getFileByteArrayFromS3(imageUrl);
        return new ByteArrayResource(imageBytes);
    }

    public GatheringImageResponse gatheringImage(Long gatheringId) {

        List<String> urls = imageRepository.gatheringImage(gatheringId);
        return GatheringImageResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .urls(urls)
                .build();
    }
}
