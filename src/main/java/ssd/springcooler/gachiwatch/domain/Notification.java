package ssd.springcooler.gachiwatch.domain;

import java.time.LocalDateTime;

public class Notification {
    private int notificationId; //알림
    private Member member; //알림을 받을 사용자
    private Content content; //새롭게 업로드 된 콘텐츠 정보
    private LocalDateTime sentAt; //이메일 전송 시간
    private String substance; //이메일로 전송될 내용
    private NotificationStatus status; //알림 상태 (PENDING, SENT, FAILED)

    // 모든 필드에 대한 생성자 -> 나중에 필요시 활성화
//    public Notification(int notificationId, Member member, Content content, LocalDateTime sentAt, String substance, NotificationStatus status) {
//        this.notificationId = notificationId;
//        this.member = member;
//        this.content = content;
//        this.sentAt = sentAt;
//        this.substance = substance;
//        this.status = status;
//    }

    // Getter & Setter
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getSubstance() {
        return substance;
    }

    public void setSubstance(String substance) {
        this.substance = substance;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", member=" + member +
                ", content=" + content +
                ", sentAt=" + sentAt +
                ", substance='" + substance + '\'' +
                ", status=" + status +
                '}';
    }
}
