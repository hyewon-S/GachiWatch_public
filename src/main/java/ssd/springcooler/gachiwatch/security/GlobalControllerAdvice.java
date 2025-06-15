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
    public Member currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            return memberService.findByEmail(userDetails.getUsername());
        }
        return null;
    }
}
