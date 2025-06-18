package ssd.springcooler.gachiwatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.CrewJoinWaiting;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.security.CustomUserDetails;
import ssd.springcooler.gachiwatch.service.CrewServiceImpl;
import ssd.springcooler.gachiwatch.service.MemberServiceImpl;
import ssd.springcooler.gachiwatch.domain.Member;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/crew/join")
public class CrewJoinController {
    @Autowired
    private CrewServiceImpl crewService;
    @Autowired
    private MemberServiceImpl memberService;

    @PostMapping
    public String joinCrew(@RequestParam Long crewId, @AuthenticationPrincipal CustomUserDetails userDetails, Principal principal) {
        // principal.getName() 등을 통해 현재 로그인 유저 정보 조회
        crewService.makeApplication(crewId, userDetails.getMember());
        return "redirect:/crew/view"; // 또는 신청 완료 페이지로 리다이렉트
    }

    @PostMapping("/accept")
    public String acceptMember(@RequestParam("crewId") Long crewId,
                               @RequestParam("memberId") Integer memberId,
                               RedirectAttributes redirectAttributes) {

        crewService.acceptMember(crewId, memberId);
        redirectAttributes.addFlashAttribute("message", "가입 승인 완료!");


        return "redirect:/crew/join?crewId=" + crewId; // 예: 신청 목록 페이지로 리다이렉트
    }

    @GetMapping
    public String showWaitingPage(@RequestParam Long crewId, @AuthenticationPrincipal CustomUserDetails userDetails, Principal principal, Model model) {
        // principal.getName() 등을 통해 현재 로그인 유저 정보 조회
        //crewService.makeApplication(crewId, userDetails.getMember());

        List<CrewJoinWaiting> crewJoinWaitingList = crewService.getWaitingList(crewId);
        List<Member> memberList = crewJoinWaitingList.stream()
                .map(waiting -> memberService.getMember(waiting.getUserId()))
                .collect(Collectors.toList());

        model.addAttribute("memberList", memberList);
        Crew crew = crewService.getCrew(crewId).get();
        model.addAttribute("crew", crew);
        model.addAttribute("loginUser", userDetails.getMember());
        List<Platform> filteredPlatforms = Arrays.stream(Platform.values())
                .filter(p -> p != Platform.NULL)
                .collect(Collectors.toList());

        model.addAttribute("platforms", filteredPlatforms);

        return "crew/join_waiting"; // 또는 신청 완료 페이지로 리다이렉트
    }
}
