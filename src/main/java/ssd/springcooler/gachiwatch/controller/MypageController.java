package ssd.springcooler.gachiwatch.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ssd.springcooler.gachiwatch.domain.Genre;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.dto.*;
import ssd.springcooler.gachiwatch.service.MemberService;
import ssd.springcooler.gachiwatch.service.ReviewService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    private final MemberService memberService;
    private final ReviewService reviewService;

    @Autowired
    public MypageController(MemberService memberService, ReviewService reviewService) {
        this.memberService = memberService;
        this.reviewService = reviewService;
    }

    // ✅ [추가] 홈 로고 클릭 시 이동할 홈 페이지
    @GetMapping("/home/home")
    public String homeRedirect() {
        return "home/home"; // ✅ 로고 클릭 시 이동할 홈 뷰
    }

    @GetMapping("/mypage")
    public String mypage(Model model, HttpSession session) {
        Member user = (Member) session.getAttribute("user");
        model.addAttribute("user", user);
        return "mypage/mypage";  // 👈 resources/templates/mypage/mypage.html
    }

    // 프로필 수정 페이지 불러오기
    @GetMapping("/my_profile")
    public String getProfile(HttpSession session, Model model) {
        Member user = (Member) session.getAttribute("user");
        model.addAttribute("user", user);
        return "mypage/my_profile"; // 화면 띄움
    }

    // 프로필 수정
    @PostMapping("/my_profile")
    public String updateProfile(ProfileUpdateDto profileUpdateDto, Model model) {
        memberService.updateProfile(profileUpdateDto);
        model.addAttribute("result", "success");
        return "redirect:/mypage";
    }

    // 내가 참여 중인 크루
    @GetMapping("/my_crew")
    public String getMyCrews(HttpSession session, Model model) {
        Member user = (Member) session.getAttribute("user");
        int memberId = (int) user.getMemberId();
        List<CrewDto> crewList = memberService.getMyCrews(memberId);
        model.addAttribute("crewList", crewList);
        return "mypage/my_crew"; // ✅ HTML 이름에 맞게 수정
    }

    //콘텐츠 목록 확인
    @GetMapping("/mypage/my_content")
    public String getMyContents(@RequestParam(defaultValue = "liked") String tab,
                                @RequestParam int memberId,
                                Model model) {
        if (tab.equals("liked")) {
            List<ContentSummaryDto> likeContent = memberService.getLikedContents(memberId);
            model.addAttribute("likedList", likeContent);
        } else if (tab.equals("watched")) {
            List<ContentSummaryDto> watchedList = memberService.getWatchedContents(memberId);
            model.addAttribute("watchedList", watchedList);
        }

        model.addAttribute("tab", tab); // 프론트에서 어떤 탭 보여줄지 판단용
        return "mypage/my_content";    // 하나의 HTML 파일에서 탭으로 나눠서 보여줌
    }

    // 작성한 리뷰 목록 조회
    @GetMapping("/my_review")
    public String getMyReviews(HttpSession session, Model model) {
        Member user = (Member) session.getAttribute("user");
        int memberId = (int) user.getMemberId();
        List<ReviewDto> reviewList = reviewService.getReviewsByUser(memberId);
        model.addAttribute("reviewList", reviewList);
        return "mypage/my_review"; // ✅ HTML 이름에 맞게 수정
    }

    // 작성한 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public String deleteReview(@PathVariable int reviewId) {
        reviewService.deleteReview(reviewId);
        return "redirect:/mypage/review";
    }

    @GetMapping("/my_subscribed_ott")
    public String getMyOtts(Model model, HttpSession session) {
        Member user = (Member) session.getAttribute("user");
        int memberId = (int) user.getMemberId();

        // NULL 제외한 플랫폼 리스트
        List<Platform> ottList = Arrays.stream(Platform.values())
                .filter(p -> p != Platform.NULL)
                .collect(Collectors.toList());
//    List<Platform> ottList = List.of(Platform.values());
        List<Platform> subscribedOttList = memberService.getSubscribedOttList(memberId);

        MemberSubscribedOttDto dto = new MemberSubscribedOttDto();
        dto.setOttList(subscribedOttList);

        model.addAttribute("ottList", ottList);
        model.addAttribute("memberSubscribedOttDto", dto);

        return "mypage/my_subscribed_ott"; // 👉 HTML 파일 이름이 ott_setting.html 이라고 가정
    }

    // 구독중인 OTT 수정
    @PostMapping("/my_subscribed_ott")
    public String updateOtt(@RequestParam List<Platform> ottList,
                            HttpSession session,
                            Model model) {
        Member user = (Member) session.getAttribute("user");
        int memberId = (int) user.getMemberId();

        memberService.updateSubscribedOtt(memberId, ottList);
        model.addAttribute("result", "success");
        return "redirect:/mypage";
    }

    // 선호 장르 페이지 보여주기
    @GetMapping("/my_preferred_genre")
    public String getMyGenres(Model model, HttpSession session) {
        Member user = (Member) session.getAttribute("user");
        int memberId = (int) user.getMemberId();

        // 전체 장르 enum 리스트
        List<Genre> allGenres = List.of(Genre.values());

        // 사용자가 선택한 선호 장르 리스트
        List<Genre> selectedGenres = memberService.getPreferredGenres(memberId);

        model.addAttribute("allGenres", allGenres);
        model.addAttribute("selectedGenres", selectedGenres);

        return "mypage/my_preferred_genre"; // HTML 위치: templates/mypage/my_preferred_genre.html
    }


    // 선호 장르 수정하기
    @PostMapping("/my_preferred_genre")
    public String updateGenre(@RequestParam int memberId,
                              @RequestParam List<Genre> genreList,
                              Model model) {
        memberService.updatePreferredGenre(memberId, genreList);
        model.addAttribute("result", "success");
        return "redirect:/mypage/my_preferred_genre"; // 수정 후 다시 장르 페이지로 이동
    }


    @GetMapping("/my_report")
    public String getReports(HttpSession session, Model model) {
        Member user = (Member) session.getAttribute("user");
        int memberId = (int) user.getMemberId();

        List<ReportDto> reportList = memberService.getReports(memberId);
        model.addAttribute("reportList", reportList);
        return "mypage/my_report"; // ✅ HTML 이름에 맞게 수정
    }
}
