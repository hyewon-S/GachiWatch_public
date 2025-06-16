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
/*
    @GetMapping("/chat")
    public String getChats() {
        return "crew/crewpage";
    }
*/
    @RequestMapping("/chat")
    public String writeChat(@RequestParam Long crewId,
                            @RequestParam("message") String chatMessage,
                            @AuthenticationPrincipal CustomUserDetails userDetails,
                            HttpSession session) {
        Member member = userDetails.getMember();
        Crew crew = crewService.getCrew(crewId).get();

        Date now = new Date();
        CrewChat newChat = new CrewChat(crew, now, chatMessage, member);

        crewService.insertCrewChat(newChat);

        // 5. 채팅 후 다시 해당 크루 페이지로 리다이렉트
        return "redirect:/crew/crewpage/" + crewId;
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

        model.addAttribute("crew", crew);
        //model.addAttribute("chatList", chatList);
        model.addAttribute("chatList", chatDtoList);

        for (ChatDto chatDto : chatDtoList) {
            System.out.println("Crew: " + chatDto.getCrew());
            System.out.println("Member Nickname: " + (chatDto.getMember() != null ? chatDto.getMember().getNickname() : "null"));
            System.out.println("Chat: " + chatDto.getChat());
            System.out.println("Date: " + chatDto.getDate());
            System.out.println("---------------------------");
        }


        return "crew/crewpage";
    }
}