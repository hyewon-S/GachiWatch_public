package ssd.springcooler.gachiwatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Notification;
import ssd.springcooler.gachiwatch.domain.NotificationStatus;
import ssd.springcooler.gachiwatch.dto.EmailNotiDto;
import ssd.springcooler.gachiwatch.dto.EmailNotiRequestDto;
import org.springframework.mail.SimpleMailMessage;
import ssd.springcooler.gachiwatch.repository.NotificationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class EmailNotiService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TMDBService tmdbService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Async //로그인시 속도 향상을 위해 추가
    public void sendRecommendationEmail(EmailNotiRequestDto dto) throws Exception {
        // 1. 오늘 이미 보냈는지 확인
        boolean alreadySentToday = notificationRepository.existsByMemberIdAndDate(dto.getMemberId(),LocalDate.now());
        if (alreadySentToday) {
            System.out.println("오늘 이미 이메일을 전송한 사용자입니다. 재전송 생략.");
            return; // 더 이상 진행하지 않음
        }

        // 2. 추천 콘텐츠 조회
        EmailNotiDto content = tmdbService.getTopPopularContentByGenres(dto.getPreferredGenres());

        if (content == null) {
            throw new IllegalStateException("추천할 콘텐츠를 찾을 수 없습니다.");
        }

        // 3. 이메일 본문 구성
        String messageText = String.format(
                "%s님 %s 장르의 %s은(는) 어떠세요? %s에서 시청 가능합니다.",
                dto.getNickname(),
                content.getGenre(),
                content.getTitle(),
                content.getAvailablePlatform()
        );

        // 4. 이메일 전송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(dto.getEmail()/*"ehwhgod0712@gmail.com"*/); // 실제 수신자 이메일로 교체 필요
        message.setSubject("GachiWatch - 당신을 위한 오늘의 콘텐츠 추천");
        message.setText(messageText);

        mailSender.send(message);

        // 4. Notification DB 저장
        Member member = memberService.findByEmail(dto.getEmail());

        if (member != null) {
            Notification notification = Notification.builder()
                    .member(member)
                    .email(dto.getEmail())
                    .nickname(dto.getNickname())
                    .content(messageText)
                    .sentAt(LocalDateTime.now())
                    .notiDate(LocalDate.now())
                    .status(NotificationStatus.SENT)
                    .build();

            notificationRepository.save(notification);
        }
    }
}
