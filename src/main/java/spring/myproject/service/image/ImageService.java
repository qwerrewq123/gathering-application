package spring.myproject.service.image;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import spring.myproject.dto.response.image.GatheringImageResponse;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.s3.S3ImageDownloadService;

import java.io.IOException;
import java.util.List;

import static spring.myproject.utils.ConstClass.SUCCESS_CODE;
import static spring.myproject.utils.ConstClass.SUCCESS_MESSAGE;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    private final S3ImageDownloadService s3ImageDownloadService;
    private final ImageRepository imageRepository;

    @Value("${server.url}")
    private String url;
    public Resource image(String imageUrl) throws IOException {
        byte[] imageBytes = s3ImageDownloadService.getFileByteArrayFromS3(imageUrl);
        return new ByteArrayResource(imageBytes);
    }

    public GatheringImageResponse gatheringImage(Long gatheringId) {

        List<String> fetchUrls = imageRepository.gatheringImage(gatheringId);
        List<String> urls = toList(fetchUrls);
        return GatheringImageResponse.of(SUCCESS_CODE, SUCCESS_MESSAGE, urls);
    }

    private List<String> toList(List<String> urls){
        return urls.stream().map(u -> getUrl(u))
                .toList();
    }
    private String getUrl(String fileUrl){
        return url+fileUrl;
    }
}
