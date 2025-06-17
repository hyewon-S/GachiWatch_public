package ssd.springcooler.gachiwatch.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ssd.springcooler.gachiwatch.domain.Crew;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ssd.springcooler.gachiwatch.domain.Genre;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.dto.*;
import ssd.springcooler.gachiwatch.service.CrewServiceImpl;

import ssd.springcooler.gachiwatch.security.CustomUserDetails;
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
    private final CrewServiceImpl crewService;

    @Autowired
    public MypageController(MemberService memberService, ReviewService reviewService, CrewServiceImpl crewService) {
        this.memberService = memberService;
        this.reviewService = reviewService;
        this.crewService = crewService;
    }

    @GetMapping("/home/home")
    public String homeRedirect() {
        return "home/home";
    }

    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", member);
        return "mypage/mypage";
    }

    // 프로필 수정 페이지 불러오기
    @GetMapping("/my_profile")
    public String getProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", member);
        return "mypage/my_profile";
    }
/*
    // 프로필 수정 페이지 불러오기
    @GetMapping("/my_profile")
    public String getProfile(HttpSession session, Model model) {
        Member user = (Member) session.getAttribute("user");
        model.addAttribute("user", user);
        return "mypage/my_profile"; // 화면 띄움
    }
*/
    // 프로필 수정
//    @PostMapping("/my_profile")
//    public String updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
//                                ProfileUpdateDto profileUpdateDto,
//                                Model model) {
//        int memberId = userDetails.getMember().getMemberId(); // 로그인한 사용자 ID 세션에서 안전하게 가져옴
//        // 여기서 클라이언트가 넘긴 memberId
//        profileUpdateDto.setMemberId(memberId); // 세션 정보로 덮어쓰기
//
//        memberService.updateProfile(profileUpdateDto); // 이제 믿을 수 있는 데이터
//        model.addAttribute("result", "success");
//        return "redirect:/mypage/mypage";
//    }
@PostMapping("/my_profile")
public String updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
                            @RequestParam(value = "password", required = false) String password,
                            @RequestParam(value = "passwordConfirm", required = false) String passwordConfirm,
                            @RequestParam(value = "nickname", required = false) String nickname,
                            RedirectAttributes redirectAttributes) {

    int memberId = userDetails.getMember().getMemberId();

    // 변경사항 체크
    boolean hasImage = profileImage != null && !profileImage.isEmpty();
    boolean hasPassword = password != null && !password.isBlank();
    boolean hasNickname = nickname != null && !nickname.isBlank();

    if (!hasImage && !hasPassword && !hasNickname) {
        redirectAttributes.addFlashAttribute("error", "변경사항이 없습니다.");
        return "redirect:/mypage/my_profile";
    }

    // 비밀번호 확인
    if (hasPassword && (passwordConfirm == null || !password.equals(passwordConfirm))) {
        redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
        return "redirect:/mypage/my_profile";
    }

    // DTO 만들기
    ProfileUpdateDto dto = ProfileUpdateDto.builder()
            .memberId(memberId)
            .nickname(hasNickname ? nickname : null)
            .password(hasPassword ? password : null)
            .build();

    // 서비스 호출
//    memberService.updateProfile(dto, profileImage);
    memberService.updateProfile(dto);


    redirectAttributes.addFlashAttribute("result", "success");
    return "redirect:/mypage/mypage";
}




    // 내가 참여 중인 크루
    @GetMapping("/my_crew")
    public String getMyCrews(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        List<Crew> crewList = crewService.getCrewListByMemberId(member.getMemberId());
        model.addAttribute("crewList", crewList);
        return "mypage/my_crew";
    }

    // 콘텐츠 목록 확인
    @GetMapping("/mypage/my_content")
    public String getMyContents(@RequestParam(defaultValue = "liked") String tab,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        int memberId = member.getMemberId();

        if ("liked".equals(tab)) {
            List<ContentSummaryDto> likeContent = memberService.getLikedContents(memberId);
            model.addAttribute("likedList", likeContent);
        } else if ("watched".equals(tab)) {
            List<ContentSummaryDto> watchedList = memberService.getWatchedContents(memberId);
            model.addAttribute("watchedList", watchedList);
        }

        model.addAttribute("tab", tab);
        return "mypage/my_content";
    }

    // 작성한 리뷰 목록 조회
    @GetMapping("/my_review")
    public String getMyReviews(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        List<ReviewDto> reviewList = reviewService.getReviewsByUser(member.getMemberId());
        model.addAttribute("reviewList", reviewList);
        return "mypage/my_review";
    }

    // 작성한 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public String deleteReview(@PathVariable int reviewId) {
        reviewService.deleteReview(reviewId);
        return "redirect:/mypage/review";
    }

    @GetMapping("/my_subscribed_ott")

    public String getMyOtts(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Member member = memberService.findByEmail(userDetails.getUsername());

        List<Platform> ottList = Arrays.stream(Platform.values())
                .filter(p -> p != Platform.NULL)
                .toList();
        List<Platform> subscribedOttList = memberService.getSubscribedOttList(member.getMemberId());

        MemberSubscribedOttDto dto = new MemberSubscribedOttDto();
        dto.setOttList(subscribedOttList);

        model.addAttribute("ottList", ottList);
        model.addAttribute("memberSubscribedOttDto", dto);

        return "mypage/my_subscribed_ott";
    }

    // 구독중인 OTT 수정
    @PostMapping("/my_subscribed_ott")
    public String updateOtt(@RequestParam List<Platform> ottList,

                            @AuthenticationPrincipal UserDetails userDetails,
                            Model model) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        memberService.updateSubscribedOtt(member.getMemberId(), ottList);

        model.addAttribute("result", "success");
        return "redirect:/mypage";
    }

    // 선호 장르 페이지 보여주기
    @GetMapping("/my_preferred_genre")
    public String getMyGenres(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Member member = memberService.findByEmail(userDetails.getUsername());

        List<Genre> allGenres = List.of(Genre.values());
        List<Genre> selectedGenres = memberService.getPreferredGenres(member.getMemberId());

        model.addAttribute("allGenres", allGenres);
        model.addAttribute("selectedGenres", selectedGenres);

        return "mypage/my_preferred_genre";
    }

    // 선호 장르 수정하기
    @PostMapping("/my_preferred_genre")
    public String updateGenre(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam List<Genre> genreList,
                              Model model) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        memberService.updatePreferredGenre(member.getMemberId(), genreList);
        model.addAttribute("result", "success");
      
        return "redirect:/mypage/my_preferred_genre";
    }

    @GetMapping("/my_report")
    public String getReports(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        List<ReportDto> reportList = memberService.getReports(member.getMemberId());
        model.addAttribute("reportList", reportList);
        return "mypage/my_report";
    }
}
