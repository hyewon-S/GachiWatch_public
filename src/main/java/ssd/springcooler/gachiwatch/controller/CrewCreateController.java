package ssd.springcooler.gachiwatch.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.security.CustomUserDetails;
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
    public String createCrew (@Valid @ModelAttribute("crewForm") CrewCommand crewCommand,
                              BindingResult bindingResult,
                              Crew crew,
                              Model model,
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              HttpSession session, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            List<Platform> filteredPlatforms = Arrays.stream(Platform.values())
                    .filter(p -> p != Platform.NULL)
                    .collect(Collectors.toList());

            model.addAttribute("platforms", filteredPlatforms);
            return "crew/create"; // 폼 화면 이름
        }

        Member captain = memberService.getMember(userDetails.getMember().getMemberId());
        Crew newCcrew = crewService.createCrew(crew, captain);

        redirectAttributes.addAttribute("id", crew.getCrewId());

        return "redirect:/crew/crewpage/" + crew.getCrewId();
    }

}