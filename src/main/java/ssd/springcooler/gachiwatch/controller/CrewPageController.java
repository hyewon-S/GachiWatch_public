package ssd.springcooler.gachiwatch.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.CrewChat;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.dto.ChatDto;
import ssd.springcooler.gachiwatch.dto.CrewDto;
import ssd.springcooler.gachiwatch.security.CustomUserDetails;
import ssd.springcooler.gachiwatch.service.CrewServiceImpl;
import ssd.springcooler.gachiwatch.service.MemberServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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

    @RequestMapping
    public String handle() {
        return "redirect:/account/login";
    }

    @GetMapping("/{id}")
    public String viewCrewPage(@PathVariable Long id, Model model) {
        Crew crew = crewService.getCrew(id).get();
        List<CrewChat> chatList = crewService.getCrewChat(id);

        List<ChatDto> chatDtoList = chatList.stream()
                .map(chat -> ChatDto.builder()
                        .crew(crewService.getCrew(chat.getCrewId()).get())
                        .member(memberService.getMember(chat.getMemberId()))
                        .chat(chat.getChat())
                        .date(chat.getChatDate())
                        .build())
                .collect(Collectors.toList());

        Member captain = null;
        if (crew.getCaptain() != null) {
            captain = memberService.getMember(crew.getCaptain().getMemberId());
        }
        else {
            captain = new Member();
            captain.setNickname("탈퇴한 사용자");
        }

        model.addAttribute("crew", crew);
        model.addAttribute("captain", captain);
        model.addAttribute("chatList", chatDtoList);

        return "crew/crewpage";
    }
}