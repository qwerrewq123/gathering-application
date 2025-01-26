package spring.myproject.domain.gathering.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.domain.category.repository.CategoryRepository;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.gathering.repository.GatheringRepository;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.user.repository.UserRepository;
import spring.myproject.dto.request.gathering.GatheringRequest;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class GatheringService {

    private final GatheringRepository gatheringRepository;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public void addGathering(GatheringRequest gatheringRequest) {


//        Gathering.builder()
//                .title(gatheringRequest.getTitle())
//                .content(gatheringRequest.getContent())
//                .participatedBy()
//                .createBy()
//                .category()
//                .registerDate(LocalDateTime.now())
//                .gatheringImage()
//                .build()
    }
}
