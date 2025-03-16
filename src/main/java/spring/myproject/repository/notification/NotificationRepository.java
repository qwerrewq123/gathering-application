package spring.myproject.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.notification.Notification;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
