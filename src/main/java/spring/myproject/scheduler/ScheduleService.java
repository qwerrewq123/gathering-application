package spring.myproject.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import spring.myproject.cache.EvictCacheService;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final EvictCacheService evictCacheService;
    @Scheduled(cron = "0 10 0 * * *")
    public void scheduledCacheEvict() {
        evictCacheService.evictCache(LocalDate.now().minusDays(1));
    }
}
