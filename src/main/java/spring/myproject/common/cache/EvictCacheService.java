package spring.myproject.common.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EvictCacheService {
    @CacheEvict(value = "recommend", key = "#localDate")
    public void evictCache(LocalDate localDate) {
    }
}
