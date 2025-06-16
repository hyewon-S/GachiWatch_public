package ssd.springcooler.gachiwatch.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.CrewChat;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.dto.CrewDto;
import ssd.springcooler.gachiwatch.service.CrewServiceImpl;
import ssd.springcooler.gachiwatch.service.MemberServiceImpl;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/crew/crewpage")
public class CrewPageController {
    @Autowired
    private CrewServiceImpl crewService;

    @Autowired
    private MemberServiceImpl memberService;
    public void setCrewServiceImpl(CrewServiceImpl crewService) {
        this.crewService = crewService;
    }

    /*
    @GetMapping
    public String getCrewPage() {
        return "crew/crewpage";
    }
*/
    @RequestMapping
    public String handle() {
        return "redirect:/account/login";
    }

    @GetMapping("/chat")
    public String getChats() {
        return "crew/crewpage";
    }

    @PostMapping("/chat")
    public String writeChat(@RequestParam Long crewId,
                            @RequestParam("message") String chatMessage,
                            HttpSession session) {
        // 1. 세션에서 로그인한 멤버 정보 얻기 (예시: memberId가 세션에 저장되어 있다고 가정)
        Integer memberId = (int) session.getAttribute("memberId");

        // 2. 크루와 멤버 엔티티 조회
        Crew crew = crewService.getCrew(crewId).get();
        Member member = memberService.getMember(memberId);

        // 3. 현재 시간으로 CrewChat 생성
        Date now = new Date();
        CrewChat newChat = new CrewChat(crew, now, chatMessage, member);

        // 4. 저장
        crewService.insertCrewChat(newChat);

        // 5. 채팅 후 다시 해당 크루 페이지로 리다이렉트
        return "redirect:/crew/crewpage/" + crewId;
    }

    @GetMapping("/{id}")
    public String viewCrewPage(@PathVariable Long id, Model model) {

        Crew crew = crewService.getCrew(id).get();
        List<CrewChat> chatList = crewService.getCrewChat(id);

        model.addAttribute("crew", crew);
        model.addAttribute("chatList", chatList);

        return "crew/crewpage";
    }
}