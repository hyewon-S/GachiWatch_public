package ssd.springcooler.gachiwatch.domain;
import lombok.Getter;

@Getter
public enum NotificationStatus {
    PENDING("대기 중"), //알림 대기 상태
    SENT("전송 완료"), //알림 전송 완료
    FAILED("전송 실패"); //알림 전송 실패

    private final String label;

    NotificationStatus(String label) {
        this.label = label;
    }
}
