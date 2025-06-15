package ssd.springcooler.gachiwatch.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.service.MemberServiceImpl;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private MemberServiceImpl memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String email = authentication.getName();  // 로그인한 사용자의 이메일
        Member member = memberService.findByEmail(email);

        HttpSession session = request.getSession();
        session.setAttribute("user", member);  // 세션에 저장

        response.sendRedirect("/home");  // 로그인 후 홈으로 이동
    }
}
