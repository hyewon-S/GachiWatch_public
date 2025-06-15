package ssd.springcooler.gachiwatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.service.CrewServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/crew/view")
public class CrewViewController {
    @Autowired
    private CrewServiceImpl crewService;
    public void setCrewServiceImpl(CrewServiceImpl crewService) {
        this.crewService = crewService;
    }

    @GetMapping
    public String viewCrews(@RequestParam(defaultValue = "0") int page, Model model) {
        if (page < 0) page = 0;
        List<Platform> filteredPlatforms = Arrays.stream(Platform.values())
                .filter(p -> p != Platform.NULL)
                .collect(Collectors.toList());

        model.addAttribute("platforms", filteredPlatforms);
/*
        List<Crew> crewList = crewService.getCrewList();
        model.addAttribute("parties", crewList);

 */
        Page<Crew> crewPage = crewService.getAllCrews(page);
        model.addAttribute("crewPage", crewPage);
        return "crew/view";
    }

    @GetMapping(params = "platform")
    public String viewCrewsByPlatform(@RequestParam String platform,
                                            @RequestParam(defaultValue = "0") int page,
                                            Model model) {
        if (page < 0) page = 0;
        //ModelAndView mav = new ModelAndView();
        List<Platform> filteredPlatforms = Arrays.stream(Platform.values())
                .filter(p -> p != Platform.NULL)
                .collect(Collectors.toList());

        model.addAttribute("platforms", filteredPlatforms);

        Page<Crew> crewPage = crewService.getAllCrewsByPlatform(platform, page);
        model.addAttribute("crewPage", crewPage);
        model.addAttribute("selectedPlatform", platform);

        //List<Crew> crewList = crewService.getCrewList();
        //mav.addObject("parties", crewList);
        return "crew/view";
    }
}