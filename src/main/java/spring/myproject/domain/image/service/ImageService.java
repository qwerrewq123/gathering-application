package spring.myproject.domain.image.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import spring.myproject.s3.S3ImageDownloadService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    private final S3ImageDownloadService s3ImageDownloadService;

    public Resource image(String imageUrl) throws IOException {
        byte[] imageBytes = s3ImageDownloadService.getFileByteArrayFromS3(imageUrl);
        return new ByteArrayResource(imageBytes);
    }
}
