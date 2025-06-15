package ssd.springcooler.gachiwatch.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.service.CrewServiceImpl;
import ssd.springcooler.gachiwatch.service.MemberService;
import ssd.springcooler.gachiwatch.service.MemberServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/crew/create")
public class CrewCreateController {
    @Autowired
    private CrewServiceImpl crewService;

    @Autowired
    private MemberServiceImpl memberService;

    public void setCrewServiceImpl(CrewServiceImpl crewService) {
        this.crewService = crewService;
    }

    @GetMapping
    public String shoeCrewCreatePage(Model model, HttpSession session) {
        model.addAttribute("crewForm", new CrewCommand());

        List<Platform> filteredPlatforms = Arrays.stream(Platform.values())
                .filter(p -> p != Platform.NULL)
                .collect(Collectors.toList());

        model.addAttribute("platforms", filteredPlatforms);
        return "crew/create";
    }

    @PostMapping
    public String createCrew (Crew crew, HttpSession session, RedirectAttributes redirectAttributes) {
        Member captain = memberService.getMember(101);
        Crew newCcrew = crewService.createCrew(crew, captain);
        //ModelAndView mav = new ModelAndView();
        //mav.setViewName("crew/crewpage");
        //mav.addObject("crew", newCcrew);
        //return mav;

        // 리다이렉트 시 사용할 id 저장
        redirectAttributes.addAttribute("id", crew.getCrewId());

        // redirect로 GET /crew/{id} 형태로 이동
        return "redirect:/crew/crewpage/" + crew.getCrewId();
    }

}