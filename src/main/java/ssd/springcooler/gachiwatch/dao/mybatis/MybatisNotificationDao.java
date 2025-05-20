package ssd.springcooler.gachiwatch.dao.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ssd.springcooler.gachiwatch.dao.NotificationDao;
import ssd.springcooler.gachiwatch.dao.mybatis.mapper.NotificationMapper;
import ssd.springcooler.gachiwatch.domain.Notification;

import java.util.List;

@Repository
public class MybatisNotificationDao implements NotificationDao {
    @Autowired
    private NotificationMapper notificationMapper;

    // 사용자 ID로 알림 목록 조회 (Content 포함)
    @Override
    public List<Notification> selectNotificationsWithContentByMemberId(int memberId) throws DataAccessException {
        return notificationMapper.selectNotificationsWithContentByMemberId(memberId);
    }

    // 알림 ID로 단건 조회 (Content 포함)
    @Override
    public Notification getNotification(int notificationId) throws DataAccessException {
        return notificationMapper.getNotification(notificationId);
    }

    // 알림 추가
    @Override
    @Transactional
    public void createNotification(Notification notification) throws DataAccessException {
        notificationMapper.createNotification(notification);
    }

    // 알림 삭제
    @Override
    public void removeNotification(int notificationId) throws DataAccessException {
        notificationMapper.removeNotification(notificationId);
    }

}
