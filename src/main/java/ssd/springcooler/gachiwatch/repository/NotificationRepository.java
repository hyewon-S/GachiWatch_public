package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssd.springcooler.gachiwatch.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
