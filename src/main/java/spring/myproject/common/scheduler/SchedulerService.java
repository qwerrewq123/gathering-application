package spring.myproject.common.scheduler;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.common.cache.EvictCacheService;
import spring.myproject.entity.fcm.FCMToken;
import spring.myproject.entity.fcm.FCMTokenTopic;
import spring.myproject.repository.fcm.FCMTokenRepository;
import spring.myproject.repository.fcm.FCMTokenTopicRepository;
import spring.myproject.service.fcm.FCMService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final EvictCacheService evictCacheService;
    private final FCMTokenRepository fcmTokenRepository;
    private final FCMTokenTopicRepository fcmTokenTopicRepository;
    private final FCMService fcmService;

    @Scheduled(cron = "0 10 0 * * *")
    @SchedulerLock(name = "cacheEvict")
    public void scheduledCacheEvict() {
        evictCacheService.evictCache(LocalDate.now().minusDays(1));
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @SchedulerLock(name = "unsubscribeExpiredTokens")
    @Transactional
    public void unsubscribeExpiredTokens() {
        LocalDate now = LocalDate.now();
        List<FCMToken> expiredTokens = fcmTokenRepository.findByExpirationDate(now);
        List<FCMTokenTopic> topicTokens = fcmTokenTopicRepository.findByFcmTokenIn(expiredTokens);
        List<String> tokenValues = expiredTokens.stream()
                .map(FCMToken::getTokenValue)
                .collect(Collectors.toList());

        topicTokens.forEach(topicToken -> {
            fcmService.unsubscribeFromTopic(topicToken.getTopic().getTopicName(), tokenValues);
        });
        fcmTokenTopicRepository.deleteAll(topicTokens);
        fcmTokenRepository.deleteAll(expiredTokens);
    }
}
