package ssd.springcooler.gachiwatch.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.service.CrewServiceImpl;
import ssd.springcooler.gachiwatch.service.MemberServiceImpl;

import java.util.Optional;

@Controller
@RequestMapping("/crew/update")
public class CrewUpdateController {
    @Autowired
    private CrewServiceImpl crewService;
    @Autowired
    private MemberServiceImpl memberService;
    public void setCrewServiceImpl(CrewServiceImpl crewService) {
        this.crewService = crewService;
    }

    @GetMapping
    public String form(@ModelAttribute("crewReq") CrewCommand crewCommand, @RequestParam("crewId") String crewId) {
        Long cid = Long.parseLong(crewId);
        Optional<Crew> crew = crewService.getCrew(cid);

        return "crew/updateForm";
    }

    @PostMapping
    public String update(@Valid @ModelAttribute Crew crew, Errors errors) {
        if (errors.hasErrors()) {
            return "crew/updateForm";
        }
        Crew oldCrew = crewService.getCrew(crew.getCrewId()).get();

        crew.setCaptain(memberService.getMember(oldCrew.getCaptain().getMemberId()));
        crewService.updateCrew(crew);

        return "redirect:/crew/manage/" + crew.getCrewId();
    }
}