package ssd.springcooler.gachiwatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.dto.MemberDto;
import ssd.springcooler.gachiwatch.security.CustomUserDetails;
import ssd.springcooler.gachiwatch.service.CrewServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
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

    @PostMapping("/kick/{memberId}")
    public String kickMember(
            @PathVariable Integer memberId, @RequestParam Long crewId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes
    ) {
        Member loginUser = userDetails.getMember();

        try {
            crewService.kickMember(crewId, memberId);
            redirectAttributes.addFlashAttribute("message", "강퇴가 완료되었습니다.");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "멤버를 찾을 수 없습니다: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "오류가 발생했습니다.");
        }

        return "redirect:/crew/manage?crewId=" + crewId;
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
        if (crew.getCaptain() != null) {
            crewCommand.setCaptain(crew.getCaptain());
        }
        else {
            Member member = new Member();
            member.setNickname("탈퇴한 사용자");
            crewCommand.setCaptain(member);
        }
        crewCommand.setCrewName(crew.getCrewName());
        crewCommand.setCrewDesc(crew.getCrewDesc());
        crewCommand.setPlatform(crew.getPlatform());
        crewCommand.setPayment(crew.getPayment());
        crewCommand.setPayDate(crew.getPayDate());
        crewCommand.setMaxMember(crew.getMaxMember());
        crewCommand.setAccount(crew.getAccount());

        //model.addAttribute("captain", captain);

        model.addAttribute("platforms", filteredPlatforms);
        model.addAttribute("loginUser", userDetails.getMember());
        model.addAttribute("crew", crewCommand);
        model.addAttribute("memberList", members);

        return "crew/manage";
    }

    @PostMapping("/exit/{crewId}")
    public ResponseEntity<String> exitCrew(
            @PathVariable Long crewId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member member = userDetails.getMember();

        try {
            crewService.deleteMemberByMemberId(crewId, member.getMemberId());
            return ResponseEntity.ok("탈퇴 완료");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("크루를 찾을 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("탈퇴 중 오류 발생");
        }
    }

}