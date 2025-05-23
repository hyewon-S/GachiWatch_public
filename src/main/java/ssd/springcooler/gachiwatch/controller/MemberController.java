package ssd.springcooler.gachiwatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ssd.springcooler.gachiwatch.domain.Genre;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.dto.*;
import ssd.springcooler.gachiwatch.service.MemberService;
import ssd.springcooler.gachiwatch.service.ReviewService;

import java.util.List;

@Controller
@RequestMapping("/mypage")
public class MemberController {

    private final MemberService memberService;
    private final ReviewService reviewService;

    @Autowired
    public MemberController(MemberService memberService, ReviewService reviewService) {
        this.memberService = memberService;
        this.reviewService = reviewService;
    }

    // 프로필 수정
    @PostMapping("/profile/update")
    public String updateProfile(ProfileUpdateDto profileUpdateDto, Model model) {
        memberService.updateProfile(profileUpdateDto);
        model.addAttribute("result", "success");
        return "redirect:/mypage.jsp";
    }

    // 내가 참여 중인 크루
    @GetMapping("/mycrew")
    public String getMyCrews(@RequestParam int memberId, Model model) {
        List<CrewDto> crewList = memberService.getMyCrews(memberId);
        model.addAttribute("crewList", crewList);
        return "/mypage/crews.jsp";
    }

    // 작성한 리뷰 목록 조회
    @GetMapping("/reviews")
    public String getMyReviews(@RequestParam int memberId, Model model) {
        List<ReviewDto> reviewList = reviewService.getReviewsByUser(memberId);
        model.addAttribute("reviewList", reviewList);
        return "/mypage/review.jsp";
    }

    // 작성한 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public String deleteReview(@PathVariable int reviewId) {
        reviewService.deleteReview(reviewId);
        return "redirect:/mypage/review.jsp";
    }

    // 찜한 콘텐츠 목록 보기 (찜순, OTT 필터, 정렬 가능)
    @GetMapping("/likecontent")
    public String getLikeContent(@RequestParam int memberId,
//                                 @RequestParam(required = false) String ottPlatform,
//                                 @RequestParam(required = false) String sortBy,
                                 Model model) {
        List<ContentSummaryDto> likeContent = memberService.getLikedContents(memberId);
        model.addAttribute("likecontent", likeContent);
        return "/mypage/likecontent.jsp";
    }


    // 내가 본 콘텐츠
    @GetMapping("/watched")
    public String getWatched(@RequestParam int memberId, Model model) {
        List<ContentSummaryDto> watchedList = memberService.getWatchedContents(memberId);
        model.addAttribute("watchedList", watchedList);
        return "/mypage/watched.jsp";
    }

    // 본 콘텐츠 삭제
    @PostMapping("/watched/delete")
    public String deleteWatched(@RequestParam int contentId, Model model) {
        memberService.deleteWatchedContent(contentId);
        model.addAttribute("result", "success");
        return "redirect:/mypage/watched.jsp";
    }

    // 구독중인 OTT 수정
    @PostMapping("/platform/update")
    public String updateOtt(@RequestParam int memberId,
                            @RequestParam List<Platform> ottList,
                            Model model) {
        memberService.updateSubscribedOtt(memberId, ottList);
        model.addAttribute("result", "success");
        return "redirect:/mypage.jsp";
    }

    // 선호 장르 수정
    @PostMapping("/update")
    public String updateGenre(@RequestParam int memberId,
                              @RequestParam List<Genre> genreList,
                              Model model) {
        memberService.updatePreferredGenre(memberId, genreList);
        model.addAttribute("result", "success");
        return "redirect:/mypage.jsp";
    }

    // 신고 내역
    @GetMapping("/reports")
    public String getReports(@RequestParam int memberId, Model model) {
        List<ReportDto> reportList = memberService.getReports(memberId);
        model.addAttribute("reportList", reportList);
        return "/mypage/reports.jsp";
    }
}
