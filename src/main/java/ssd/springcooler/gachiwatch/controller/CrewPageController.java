package ssd.springcooler.gachiwatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ssd.springcooler.gachiwatch.dto.CrewDto;
import ssd.springcooler.gachiwatch.service.CrewServiceImpl;

@Controller
@RequestMapping("/crew/crewpage")
public class CrewPageController {
    @Autowired
    private CrewServiceImpl crewService;
    public void setCrewServiceImpl(CrewServiceImpl crewService) {
        this.crewService = crewService;
    }

    @GetMapping
    public String getCrewPage() {
        return "crew/crewpage";
    }

    @GetMapping("/chat")
    public String getChats() {
        return "crew/crewpage";
    }

    @PostMapping("/chat")
    public String writeChat() {
        return "crew/crewpage";
    }

    @GetMapping("/{id}")
    public String viewCrewPage(@PathVariable Long id, Model model) {
        CrewDto crewDto = crewService.getCrewWithChat(id);
        model.addAttribute("crewDto", crewDto);
        //model.addAttribute("crew", dto.getCrew());
        //model.addAttribute("chatMessages", dto.getChatList());
        return "crew/crewpage";
    }
}