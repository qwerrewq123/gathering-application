package spring.myproject.domain.fcm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.fcm.FCMToken;

public interface FCMTokenRepository extends JpaRepository<FCMToken, Long> {
}
