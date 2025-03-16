package spring.myproject.entity.gathering.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import spring.myproject.repository.category.CategoryRepository;
import spring.myproject.repository.enrollment.EnrollmentRepository;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.entity.user.User;
import spring.myproject.exception.user.NotFoundUserException;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.s3.S3ImageDownloadService;
import spring.myproject.s3.S3ImageUploadService;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class GatheringServiceTest {
    @MockitoBean
    GatheringRepository gatheringRepository;
    @MockitoBean
    EnrollmentRepository enrollmentRepository;
    @MockitoBean
    ImageRepository imageRepository;
    @MockitoBean
    CategoryRepository categoryRepository;
    @MockitoBean
    UserRepository userRepository;
    @MockitoBean
    S3ImageUploadService s3ImageUploadService;
    @MockitoBean
    S3ImageDownloadService s3ImageDownloadService;
    @Test
    void addGathering() {
        when(userRepository.findByUsername("true username")).thenReturn(Optional.of(mock(User.class)));
        when(userRepository.findByUsername("false username")).thenThrow(NotFoundUserException.class);

    }

    @Test
    void updateGathering() {
    }

    @Test
    void gatheringDetail() {
    }

    @Test
    void gatherings() {
    }

    @Test
    void gatheringCategory() {
    }

    @Test
    void gatheringsLike() {
    }

    @Test
    void categorizeByCategory() {
    }
    @Test
    void saveImage(){

    }
}