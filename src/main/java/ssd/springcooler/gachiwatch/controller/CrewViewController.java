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
/*
    @GetMapping
    public String viewCrews(@RequestParam(defaultValue = "0") int page, Model model) {
        if (page < 0) page = 0;
        List<Platform> filteredPlatforms = Arrays.stream(Platform.values())
                .filter(p -> p != Platform.NULL)
                .collect(Collectors.toList());

        model.addAttribute("platforms", filteredPlatforms);

        Page<Crew> crewPage = crewService.getAllCrews(page);
        model.addAttribute("crewPage", crewPage);
        return "crew/view";
    }

    @GetMapping(params = "platform")
    public String viewCrewsByPlatform(@RequestParam String platform,
                                            @RequestParam(defaultValue = "0") int page,
                                            Model model) {
        if (page < 0) page = 0;

        List<Platform> filteredPlatforms = Arrays.stream(Platform.values())
                .filter(p -> p != Platform.NULL)
                .collect(Collectors.toList());

        model.addAttribute("platforms", filteredPlatforms);

        // 플랫폼이 빈 문자열이거나 null인 경우 기본 조회로 처리하거나 빈 결과 처리
        if (platform == null || platform.isBlank()) {
            Page<Crew> crewPage = crewService.getAllCrews(page);
            model.addAttribute("crewPage", crewPage);
            return "crew/view";
        }

        Page<Crew> crewPage = crewService.getAllCrewsByPlatform(platform, page);
        model.addAttribute("crewPage", crewPage);
        model.addAttribute("selectedPlatform", platform);

        return "crew/view";
    }
*/
/*
    @GetMapping
    public String viewCrews(@RequestParam(required = false) String platform,
                            @RequestParam(defaultValue = "0") int page,
                            Model model) {
        if (page < 0) page = 0;

        // NULL 플랫폼 제거
        List<Platform> filteredPlatforms = Arrays.stream(Platform.values())
                .filter(p -> p != Platform.NULL)
                .collect(Collectors.toList());
        model.addAttribute("platforms", filteredPlatforms);

        // 플랫폼이 없는 경우 전체 조회, 있으면 필터링 조회
        Page<Crew> crewPage;
        if (platform == null || platform.isBlank()) {
            crewPage = crewService.getAllCrews(page);
        } else {
            crewPage = crewService.getAllCrewsByPlatform(platform, page);
            model.addAttribute("selectedPlatform", platform); // 선택된 플랫폼 유지
        }

        model.addAttribute("crewPage", crewPage);
        return "crew/view";
    }
*/
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
                // 비로그인 시 로그인 페이지로 리다이렉트 혹은 에러 처리
                return "redirect:/account/login";
            }

            // 예를 들어 principal이 User 객체일 때
            Member member = (Member) auth.getPrincipal();
            int userId = (Integer) member.getMemberId();

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