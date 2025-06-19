package ssd.springcooler.gachiwatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.dto.MemberDto;
import ssd.springcooler.gachiwatch.security.CustomUserDetails;
import ssd.springcooler.gachiwatch.service.CrewServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @RequestMapping
    public String handle() {
        return "redirect:/account/login";
    }

    @GetMapping("{crewId}")
    public String showManagePage(@PathVariable Long crewId, Model model,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        Optional<Crew> crewOpt = crewService.getCrew(crewId);
        if (crewOpt.isEmpty()) {
            return "error/404";
        }

        if (userDetails.getMember() == null) {
            return "redirect:/account/login";
        }
        Crew crew = crewOpt.get();
        List<Member> members = crewService.getCrewMembersByCrewId(crewId);

        List<Platform> filteredPlatforms = Arrays.stream(Platform.values())
                .filter(p -> p != Platform.NULL)
                .collect(Collectors.toList());

        CrewCommand crewCommand = new CrewCommand();
        crewCommand.setCrewId(crew.getCrewId());
        crewCommand.setCaptain(crew.getCaptain());
        crewCommand.setCrewName(crew.getCrewName());
        crewCommand.setCrewDesc(crew.getCrewDesc());
        crewCommand.setPlatform(crew.getPlatform());
        crewCommand.setPayment(crew.getPayment());
        crewCommand.setPayDate(crew.getPayDate());
        crewCommand.setMaxMember(crew.getMaxMember());
        crewCommand.setAccount(crew.getAccount());

        model.addAttribute("platforms", filteredPlatforms);
        model.addAttribute("loginUser", userDetails.getMember());
        model.addAttribute("crew", crewCommand);
        model.addAttribute("memberList", members);

        return "crew/manage";
    }

}