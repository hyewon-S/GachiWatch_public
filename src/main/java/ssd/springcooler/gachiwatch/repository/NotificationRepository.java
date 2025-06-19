package ssd.springcooler.gachiwatch.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ssd.springcooler.gachiwatch.domain.Notification;

import java.time.LocalDate;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    //사용자 id와 날짜(시간없는)로 조회
    @Query("SELECT COUNT(n) > 0 FROM Notification n WHERE n.member.memberId = :memberId AND n.notiDate = :today")
    boolean existsByMemberIdAndDate(@Param("memberId") int memberId, @Param("today") LocalDate today);
}
