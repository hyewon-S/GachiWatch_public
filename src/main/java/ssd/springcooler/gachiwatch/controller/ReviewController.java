package ssd.springcooler.gachiwatch.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Review;
import ssd.springcooler.gachiwatch.dto.ReviewDto;
import ssd.springcooler.gachiwatch.service.MemberServiceImpl;
import ssd.springcooler.gachiwatch.service.ReviewService;

import java.util.List;

@Controller
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/form")
    public String form(@RequestParam int contentId, Model model, @AuthenticationPrincipal UserDetails userDetails) {

        if( userDetails != null) {
            Member user = memberService.findByEmail(userDetails.getUsername());
            model.addAttribute("contentId", contentId);
            model.addAttribute("memberId", user.getMemberId());
        }
        return "review/review_form";
    }

    @PostMapping("/save")
    public String save(@RequestParam int star, @RequestParam int memberId, @RequestParam int contentId, @RequestParam String reviewContent) {
        reviewService.insertReview(contentId, memberId, reviewContent, star);
        return "redirect:/content/detail?contentId=" + contentId;
    }

    @GetMapping("/updateForm")
    public String updateForm(@RequestParam int reviewId, @RequestParam int contentId, @RequestParam String substance, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if( userDetails != null) {
            model.addAttribute("reviewId", reviewId);
            model.addAttribute("substance", substance);
            model.addAttribute("contentId", contentId);
        }
        return "review/review_form";
    }

    @PostMapping("/update")
    public String update(@RequestParam int star, @RequestParam int contentId, @RequestParam int reviewId, @RequestParam String reviewContent) {
        reviewService.updateReview(reviewId, reviewContent, star);
        return "redirect:/content/detail?contentId=" + contentId;
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int reviewId, @RequestParam int contentId) {
        reviewService.deleteReview(reviewId);
        return "redirect:/content/detail?contentId=" + contentId;
    }
}
