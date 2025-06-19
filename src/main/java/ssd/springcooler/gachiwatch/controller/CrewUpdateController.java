package ssd.springcooler.gachiwatch.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.security.CustomUserDetails;
import ssd.springcooler.gachiwatch.service.CrewServiceImpl;
import ssd.springcooler.gachiwatch.service.MemberServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
/*
    @GetMapping
    public String form(@ModelAttribute("crewReq") CrewCommand crewCommand, @RequestParam("crewId") String crewId) {
        Long cid = Long.parseLong(crewId);
        Optional<Crew> crew = crewService.getCrew(cid);

        return "crew/update";
    }
*/
    @PostMapping
    public String update(@Valid @ModelAttribute("crew") CrewCommand crewCommand, Errors errors, Model model,
                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (errors.hasErrors()) {
            List<Platform> filteredPlatforms = Arrays.stream(Platform.values())
                    .filter(p -> p != Platform.NULL)
                    .collect(Collectors.toList());

            List<Member> members = crewService.getCrewMembersByCrewId(crewCommand.getCrewId());

            Crew originalCrew = crewService.getCrew(crewCommand.getCrewId()).get();  // ✅ captain 정보 포함됨
            crewCommand.setCaptain(originalCrew.getCaptain());

            model.addAttribute("platforms", filteredPlatforms);
            model.addAttribute("crew", crewCommand);
            model.addAttribute("memberList", members);
            model.addAttribute("loginUser", userDetails.getMember());

            return "crew/manage";
        }
        Crew oldCrew = crewService.getCrew(crewCommand.getCrewId()).get();
        oldCrew.setCrewName(crewCommand.getCrewName());
        oldCrew.setCrewDesc(crewCommand.getCrewDesc());
        oldCrew.setPlatform(crewCommand.getPlatform());
        oldCrew.setPayment(crewCommand.getPayment());
        oldCrew.setPayDate(crewCommand.getPayDate());
        oldCrew.setMaxMember(crewCommand.getMaxMember());
        oldCrew.setAccount(crewCommand.getAccount());
        oldCrew.setCaptain(memberService.getMember(oldCrew.getCaptain().getMemberId()));
        crewService.updateCrew(oldCrew);

        return "redirect:/crew/manage/" + oldCrew.getCrewId();
    }
}