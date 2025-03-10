package spring.myproject.domain.gathering.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import spring.myproject.domain.category.repository.CategoryRepository;
import spring.myproject.domain.enrollment.repository.EnrollmentRepository;
import spring.myproject.domain.gathering.repository.GatheringRepository;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.exception.NotFoundUserException;
import spring.myproject.domain.user.repository.UserRepository;
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