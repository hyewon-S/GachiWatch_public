package ssd.springcooler.gachiwatch.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.dto.EmailNotiRequestDto;
import ssd.springcooler.gachiwatch.service.EmailNotiService;
import ssd.springcooler.gachiwatch.service.MemberServiceImpl;
import org.springframework.security.core.context.SecurityContext;


import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Lazy //MemberService가 실제로 필요한 시점까지 초기화를 미룸
    @Autowired
    private MemberServiceImpl memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String email = authentication.getName();  // 로그인한 사용자의 이메일
        Member member = memberService.findByEmail(email);

        //로그인 정보 넘어오는지 확인용
        System.out.println("로그인 성공: " + email);
        System.out.println("member: " + member);

        // AJAX 구분 없이 무조건 홈으로 리다이렉트
        response.sendRedirect("/home");

    }
}
