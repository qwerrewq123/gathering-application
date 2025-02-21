package spring.myproject.domain.gathering.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.gathering.GatheringCount;
import spring.myproject.domain.gathering.repository.GatheringCountRepository;

@Service
@RequiredArgsConstructor
public class GatheringCountService {


    private final GatheringCountRepository gatheringCountRepository;




    public void addCount(Long gatheringId){

        gatheringCountRepository.addCount(gatheringId);

    }

    public void makeCount(Gathering gathering){

        gatheringCountRepository.save(
                GatheringCount.builder()
                        .count(1)
                        .gathering(gathering)
                        .build()
        );
    }


}
