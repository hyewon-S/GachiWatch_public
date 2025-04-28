package ssd.springcooler.gachiwatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        return "";
    }

    @GetMapping("/chat")
    public String getChats() {
        return "";
    }

    @PostMapping("/chat")
    public String writeChat() {
        return "";
    }
}