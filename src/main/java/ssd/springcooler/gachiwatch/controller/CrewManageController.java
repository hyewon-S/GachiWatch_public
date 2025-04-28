package ssd.springcooler.gachiwatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ssd.springcooler.gachiwatch.service.CrewServiceImpl;

@Controller
@RequestMapping("/crew/manage")
public class CrewManageController {
    @Autowired
    private CrewServiceImpl crewService;
    public void setCrewServiceImpl(CrewServiceImpl crewService) {
        this.crewService = crewService;
    }

    @GetMapping(value={"/join", "/join/{result}"})
    public String joinProcess(@PathVariable("result") String result) {
        return "";
    }

    @GetMapping("/kick")
    public String kickMember() {
        return "";
    }

    @GetMapping("/report")
    public String reportMember() {
        return "";
    }

}