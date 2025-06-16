package ssd.springcooler.gachiwatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ssd.springcooler.gachiwatch.security.CustomUserDetails;
import ssd.springcooler.gachiwatch.service.CrewServiceImpl;

import java.security.Principal;

@Controller
@RequestMapping("/crew/join")
public class CrewJoinController {
    @Autowired
    private CrewServiceImpl crewService;

    @PostMapping
    public String joinCrew(@RequestParam Long crewId, @AuthenticationPrincipal CustomUserDetails userDetails, Principal principal) {
        // principal.getName() 등을 통해 현재 로그인 유저 정보 조회
        crewService.makeApplication(crewId, userDetails.getMember());
        return "redirect:/crew/view"; // 또는 신청 완료 페이지로 리다이렉트
    }
}
