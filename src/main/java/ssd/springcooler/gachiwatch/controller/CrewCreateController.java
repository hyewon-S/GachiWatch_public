package ssd.springcooler.gachiwatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.service.CrewServiceImpl;

@Controller
@RequestMapping("/crew/create")
public class CrewCreateController {
    @Autowired
    private CrewServiceImpl crewService;
    public void setCrewServiceImpl(CrewServiceImpl crewService) {
        this.crewService = crewService;
    }
/*
    @PostMapping
    public ModelAndView createCrew createCrew(CrewCommand crewCommand) {
        Crew crew = crewService.createCrew(crewCommand);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("crew/crewpage");
        mav.addObject("crew", crew);
        return mav;
    }
    */
}