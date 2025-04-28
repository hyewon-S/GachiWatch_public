package ssd.springcooler.gachiwatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ssd.springcooler.gachiwatch.service.CrewServiceImpl;

@Controller
@RequestMapping("/crew/view")
public class CrewViewController {
    @Autowired
    private CrewServiceImpl crewService;
    public void setCrewServiceImpl(CrewServiceImpl crewService) {
        this.crewService = crewService;
    }

    @GetMapping
    public ModelAndView viewCrews() {
        ModelAndView mav = new ModelAndView();
        return mav;
    }
}