package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notification")
public class Notification   {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq")
    @SequenceGenerator(name = "notification_seq", sequenceName = "NOTIFICATION_SEQ", allocationSize = 1)
    @Column(name = "notification_id")
    private Long id;

    // Member와 다대일 관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private String email;          // 알림을 받은 이메일
    private String nickname;       // 회원 닉네임

    @Column(length = 1000)
    private String content;        // 알림 내용 (예: 로그인 알림 메시지)

    @Column(name = "sent_at")
    private LocalDateTime sentAt;  // 알림 전송 시간

    @Enumerated(EnumType.STRING)
    private NotificationStatus status; // 전송 상태 (예: SENT, FAILED 등)


}
