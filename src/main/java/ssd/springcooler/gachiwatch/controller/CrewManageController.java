package ssd.springcooler.gachiwatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.dto.MemberDto;
import ssd.springcooler.gachiwatch.service.CrewServiceImpl;

import java.util.List;
import java.util.Optional;

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
        //로그인 화면으로 보내기
        return "redirect:/account/login";
    }

    @GetMapping("{crewId}")
    public String showManagePage(@PathVariable Long crewId, Model model) {
        Optional<Crew> crewOpt = crewService.getCrew(crewId);
        if (crewOpt.isEmpty()) {
            return "error/404";
        }

        Crew crew = crewOpt.get();
        List<Member> members = crewService.getCrewMembers(crewId);

        /*
        // 결제일 포맷 변경: yyyy-MM-dd → MM:dd
        String formattedPayDate = "";
        if (crew.getPayDate() != null) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM:dd");

            try {
                LocalDate payDate = LocalDate.parse(crew.getPayDate(), inputFormatter);
                formattedPayDate = outputFormatter.format(payDate);
            } catch (DateTimeParseException e) {
                formattedPayDate = crew.getPayDate(); // fallback
            }
        }
*/
        model.addAttribute("crew", crew);
        model.addAttribute("members", members);

        return "crew/manage";
    }

}