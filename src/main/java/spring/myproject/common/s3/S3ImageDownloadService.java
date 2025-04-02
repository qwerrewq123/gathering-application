package spring.myproject.common.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;

@Service
public class S3ImageDownloadService {

    @Value("${s3.credentials.access-key}")
    private String accessKeyId;

    @Value("${s3.credentials.secret-key}")
    private String secretAccessKey;

    @Value("${s3.credentials.region}")
    private String region;

    @Value("${s3.bucket}")
    private String bucket;

    private final S3Client s3Client;

    public S3ImageDownloadService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public byte[] getFileByteArrayFromS3(String fileName) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();
        InputStream inputStream = s3Client.getObject(getObjectRequest);
        byte[] fileBytes = inputStream.readAllBytes();
        inputStream.close();
        return fileBytes;
    }


}
