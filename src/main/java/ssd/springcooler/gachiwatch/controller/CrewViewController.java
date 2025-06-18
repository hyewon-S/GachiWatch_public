package ssd.springcooler.gachiwatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.ModelAndView;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.security.CustomUserDetails;
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
    public String viewCrews(@RequestParam(required = false) String platform,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(required = false) Boolean myCrew,
                            Model model) {
        if (page < 0) page = 0;

        List<Platform> filteredPlatforms = Arrays.stream(Platform.values())
                .filter(p -> p != Platform.NULL)
                .collect(Collectors.toList());
        model.addAttribute("platforms", filteredPlatforms);

        Page<Crew> crewPage;

        if (Boolean.TRUE.equals(myCrew)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            System.out.println("auth = " + auth);
            System.out.println("principal = " + (auth != null ? auth.getPrincipal() : "null"));
            System.out.println("isAuthenticated = " + (auth != null && auth.isAuthenticated()));

            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
                return "redirect:/account/login";
            }


            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            Member member = (Member) userDetails.getMember();
            int userId = (Integer) member.getMemberId();
            System.out.println(userId);

            crewPage = crewService.getCrewListByMemberId(userId, page);
            model.addAttribute("myCrew", true);
        }
        else if (platform != null && !platform.isBlank()) {
            crewPage = crewService.getAllCrewsByPlatform(platform, page);
            model.addAttribute("selectedPlatform", platform);
        }
        else {
            crewPage = crewService.getAllCrews(page);
        }

        model.addAttribute("crewPage", crewPage);
        return "crew/view";
    }

}