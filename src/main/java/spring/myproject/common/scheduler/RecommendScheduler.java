package spring.myproject.common.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.repository.recommend.RecommendRepository;

@RequiredArgsConstructor
@Component
@Transactional
public class RecommendScheduler {
    //TODO :고민
    private final RecommendRepository recommendRepository;
    @Scheduled(cron = "0 0 0 * * ?")
    public void executeAtMidnight() {
    }
}
