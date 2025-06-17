package ssd.springcooler.gachiwatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ssd.springcooler.gachiwatch.dto.EmailNotiDto;
import ssd.springcooler.gachiwatch.dto.EmailNotiRequestDto;
import org.springframework.mail.SimpleMailMessage;

@Service
public class EmailNotiService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TMDBService tmdbService;

    public void sendRecommendationEmail(EmailNotiRequestDto dto) throws Exception {
        // 1. 추천 콘텐츠 조회
        EmailNotiDto content = tmdbService.getTopPopularContentByGenres(dto.getPreferredGenres());

        if (content == null) {
            throw new IllegalStateException("추천할 콘텐츠를 찾을 수 없습니다.");
        }

        // 2. 이메일 본문 구성
        String messageText = String.format(
                "%s님 %s 장르의 %s은(는) 어떠세요? %s에서 시청 가능합니다.",
                dto.getNickname(),
                content.getGenre(),
                content.getTitle(),
                content.getAvailablePlatform()
        );

        // 3. 이메일 전송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(dto.getEmail()/*"ehwhgod0712@gmail.com"*/); // 실제 수신자 이메일로 교체 필요
        message.setSubject("GachiWatch - 당신을 위한 오늘의 콘텐츠 추천");
        message.setText(messageText);

        mailSender.send(message);
    }
}
