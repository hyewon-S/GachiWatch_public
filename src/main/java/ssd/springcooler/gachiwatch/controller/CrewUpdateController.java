package ssd.springcooler.gachiwatch.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.service.CrewServiceImpl;

@Controller
@RequestMapping("/crew/update")
public class CrewUpdateController {
    @Autowired
    private CrewServiceImpl crewService;
    public void setCrewServiceImpl(CrewServiceImpl crewService) {
        this.crewService = crewService;
    }

    @GetMapping
    public String form(@ModelAttribute("crewReq") CrewCommand crewCommand, @RequestParam("crewId") String crewId) {
        int cid = Integer.parseInt(crewId);
        Crew crew = crewService.getCrew(cid);
        //crewCommand setting
        /* ... */

        return "crew/updateForm";
    }

    @PostMapping
    public ModelAndView update(@Valid @ModelAttribute("crewReq") CrewCommand crewCommand, Errors errors) {
        if (errors.hasErrors()) {
            ModelAndView mav = new ModelAndView("crew/updateForm");
            return mav;
        }

        Crew crew = new Crew();
        //crew = crewService.updateCrew(crew);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("crew/crewpage");
        mav.addObject("crew", crew);
        return mav;
    }
}