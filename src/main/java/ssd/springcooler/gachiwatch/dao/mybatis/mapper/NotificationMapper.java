package ssd.springcooler.gachiwatch.dao.mybatis.mapper;
import org.apache.ibatis.annotations.Mapper;
import ssd.springcooler.gachiwatch.domain.Notification;

import java.util.List;

@Mapper
public interface NotificationMapper {
    // 알림 생성
    void createNotification(Notification notification);

    // 알림 삭제
    void removeNotification(int notificationId);

    // 알림 ID로 알림 1건 조회 (Content 포함)
    Notification getNotification(int notificationId);

    // 사용자 ID로 알림 목록 조회 (Content 포함)
    List<Notification> selectNotificationsWithContentByMemberId(int memberId);
}
