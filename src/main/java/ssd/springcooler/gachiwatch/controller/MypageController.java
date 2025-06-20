package ssd.springcooler.gachiwatch.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
import ssd.springcooler.gachiwatch.repository.MemberRepository;


import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    private final MemberService memberService;
    private final ReviewService reviewService;
    private final CrewServiceImpl crewService;
    private final MemberRepository memberRepository;


    @Autowired
    public MypageController(MemberService memberService, ReviewService reviewService, CrewServiceImpl crewService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.reviewService = reviewService;
        this.crewService = crewService;
        this.memberRepository = memberRepository;
    }

    /** 마이페이지 불러오기 */
    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", member);
        return "mypage/mypage";
    }

    /** 프로필 수정 페이지 불러오기 */
    @GetMapping("/my_profile")
    public String getProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", member);
        return "mypage/my_profile";
    }

    /** 프로필 수정 */
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

        memberService.updateProfile(dto);

        redirectAttributes.addFlashAttribute("result", "success");
        return "redirect:/mypage/mypage";
    }

    @GetMapping("/account/check-nickname")
    @ResponseBody
    public Map<String, Boolean> checkNickname(@RequestParam String nickname,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean exists = memberRepository.existsByNickname(nickname);

        // 본인 닉네임이면 중복 아님 처리
        if (userDetails != null && userDetails.getMember().getNickname().equals(nickname)) {
            exists = false;
        }

        return Collections.singletonMap("exists", exists);
    }

    /** 내 크루 페이지 불러오기 */
    @GetMapping("/my_crew")
    public String getMyCrews(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        List<Crew> crewList = crewService.getCrewListByMemberId(member.getMemberId());
        model.addAttribute("crewList", crewList);
        return "mypage/my_crew";
    }

    /** 찜했어요/봤어요 콘텐츠 목록 페이지 불러오기 */
    @GetMapping("/my_content")
    public String getMyContents(@RequestParam(defaultValue = "liked") String tab,
                                @RequestParam(required = false) String otts,
                                @RequestParam(required = false) String types,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {

        Member member = memberService.findByEmail(userDetails.getUsername());
        int memberId = member.getMemberId();

        List<String> selectedOtts = (otts == null || otts.isBlank())
                ? List.of("NETFLIX", "WATCHA", "APPLE", "WAVVE", "DISNEY", "AMAZON")
                : Arrays.asList(otts.split(","));

        List<String> selectedTypes = (types == null || types.isBlank())
                ? List.of("MOVIE", "TV")
                : Arrays.asList(types.split(","));

        List<ContentSummaryDto> contentList =
                "liked".equals(tab)
                        ? memberService.getLikedContents(memberId, selectedOtts, selectedTypes)
                        : memberService.getWatchedContents(memberId, selectedOtts, selectedTypes);

        model.addAttribute("contentList", contentList);
        model.addAttribute("tab", tab);
        model.addAttribute("otts", selectedOtts);
        model.addAttribute("types", selectedTypes);

        // 필요하면 로그로 변경
        System.out.println("선택된 OTT: " + selectedOtts);
        System.out.println("선택된 타입: " + selectedTypes);

        return "mypage/my_content";
    }


    /** 내 리뷰 페이지 불러오기 */
    @GetMapping("/my_review")
    public String getMyReviews(@AuthenticationPrincipal UserDetails userDetails,
                               @RequestParam(value = "sort", defaultValue = "desc") String sort,
                               Model model) {
        Member member = memberService.findByEmail(userDetails.getUsername());
        List<MemberReviewDto> reviewList = memberService.findMyReviews(member.getMemberId(), sort);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("currentSort", sort);
        return "mypage/my_review";
    }

    /** 내 리뷰 삭제 */
    @PostMapping("/delete_reviews")
    public String deleteMyReviews(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestParam(name = "reviewIds", required = false) List<Integer> reviewIds,
                                  RedirectAttributes redirectAttributes) {
        if (reviewIds != null && !reviewIds.isEmpty()) {
            Member member = memberService.findByEmail(userDetails.getUsername());
            memberService.deleteMyReviews(member.getMemberId(), reviewIds);
            redirectAttributes.addFlashAttribute("message", "리뷰가 삭제되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("message", "선택된 리뷰가 없습니다.");
        }
        return "redirect:/mypage/my_review";
    }

    /** 내 구독 OTT 페이지 불러오기 */
    @GetMapping("/my_subscribed_ott")
    public String getMyOttList(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer memberId = userDetails.getMemberId();

        List<Platform> allOttList = Arrays.stream(Platform.values())
                .filter(p -> p != Platform.NULL)  // NULL 제외
                .toList();

        List<Platform> subscribedOttList = memberService.findMyOttList(memberId)
                .stream()
                .filter(p -> p != Platform.NULL) // NULL 제외
                .toList();

        MemberSubscribedOttDto dto = new MemberSubscribedOttDto();
        dto.setOttList(subscribedOttList);

        model.addAttribute("ottList", allOttList);
        model.addAttribute("memberSubscribedOttDto", dto);

        return "mypage/my_subscribed_ott";
    }

    /** 내 구독 OTT 목록 수정 */
    @PostMapping("/my_subscribed_ott")
    public String updateMyOttList(@ModelAttribute MemberSubscribedOttDto dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer memberId = userDetails.getMemberId();

        memberService.updateMyOttList(memberId, dto.getOttList());

        return "redirect:/mypage/mypage";
    }

    /** 내 선호 장르 페이지 불러오기 */
    @GetMapping("/my_preferred_genre")
    public String getMyGenreList(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Member member = memberService.findByEmail(userDetails.getUsername());

        List<Genre> allGenres = List.of(Genre.values());
        List<Genre> selectedGenres = memberService.findMyGenreList(member.getMemberId());

        model.addAttribute("allGenres", allGenres);
        model.addAttribute("selectedGenres", selectedGenres);

        return "mypage/my_preferred_genre";
    }

    /** 내 선호 장르 목록 수정 */
    @PostMapping("/my_preferred_genre")
    public String updateMyGenreList(@AuthenticationPrincipal UserDetails userDetails,
                                    @RequestParam(value = "genreList", required = false) List<Genre> genreList,
                                    RedirectAttributes redirectAttributes) {
        Member member = memberService.findByEmail(userDetails.getUsername());

        // null이면 빈 리스트로 처리
        if (genreList == null) {
            genreList = List.of();
        }

        memberService.updateMyGenreList(member.getMemberId(), genreList);
        redirectAttributes.addFlashAttribute("result", "success");

        return "redirect:/mypage/mypage";
    }
}
