package ssd.springcooler.gachiwatch.dao;
import java.util.List;
import ssd.springcooler.gachiwatch.domain.Notification;

public interface NotificationDao {

    void createNotification(Notification notification);
    void removeNotification(int notificationId);
    Notification getNotification(int notificationId);
    List<Notification> selectNotificationsWithContentByMemberId(int memberId);
}
