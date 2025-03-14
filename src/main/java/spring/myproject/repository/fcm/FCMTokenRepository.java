package spring.myproject.repository.fcm;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.FCMToken;

public interface FCMTokenRepository extends JpaRepository<FCMToken, Long> {
}
