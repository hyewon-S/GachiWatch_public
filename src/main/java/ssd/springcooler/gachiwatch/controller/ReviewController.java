package ssd.springcooler.gachiwatch.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ssd.springcooler.gachiwatch.domain.Member;

@Controller
@RequestMapping("/review")
public class ReviewController {
    @GetMapping("/form")
    public String form(@RequestParam int contentId, Model model, HttpSession session) {
        Member user = (Member)session.getAttribute("user");
        model.addAttribute("contentId", contentId);
        model.addAttribute("memberId", user.getMemberId());
        return "review/review_form";
    }

    @PostMapping("/save")
    public String save(@RequestParam int star, @RequestParam String memberId, @RequestParam String contentId, @RequestParam String reviewContent) {
        System.out.println("star test : " + star);
        System.out.println("contentId test : " + contentId);
        System.out.println("memberId test : " + memberId);
        System.out.println("reviewContnet test : " + reviewContent);
        return "redirect:/content/detail?contentId=" + contentId;
    }
}
