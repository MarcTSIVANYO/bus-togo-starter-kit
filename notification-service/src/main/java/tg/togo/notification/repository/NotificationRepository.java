package tg.togo.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.togo.notification.modele.Notification;

public interface NotificationRepository extends JpaRepository<Notification, String> {}
