package ssd.springcooler.gachiwatch.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.service.MemberServiceImpl;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private MemberServiceImpl memberService;

    @ModelAttribute("user")
    public Member currentUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        System.out.println("🔥 GlobalControllerAdvice 실행됨");
        System.out.println("🔥 customUserDetails: " + customUserDetails);
    //    return (customUserDetails != null) ? customUserDetails.getMember() : null;
        Member member = null;
        if (customUserDetails != null) {
            member = customUserDetails.getMember();
            System.out.println("🔥 member: " + member);
        }
        return member;
}

}
