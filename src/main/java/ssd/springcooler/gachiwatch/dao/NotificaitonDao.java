package ssd.springcooler.gachiwatch.dao;
import java.util.List;
import ssd.springcooler.gachiwatch.domain.Notification;

public interface NotificaitonDao {
    void insertNotification(Notification notification);
    Notification getNotification(int notificationId);
    List<Notification> getNotificationsByUser(int userId);
    void updateNotificationStatus(int notificationId, NotificationStatus status);
    void deleteNotification(int notificationId);
}
