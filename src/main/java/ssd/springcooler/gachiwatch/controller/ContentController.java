package ssd.springcooler.gachiwatch.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Review;
import ssd.springcooler.gachiwatch.dto.ContentDto;
import ssd.springcooler.gachiwatch.dto.ContentSummaryDto;
import ssd.springcooler.gachiwatch.dto.ForMeContentDto;
import ssd.springcooler.gachiwatch.dto.ReviewDto;
import ssd.springcooler.gachiwatch.security.CustomUserDetails;
import ssd.springcooler.gachiwatch.service.ContentService;
import ssd.springcooler.gachiwatch.service.MemberServiceImpl;
import ssd.springcooler.gachiwatch.service.ReviewServiceImpl;
import ssd.springcooler.gachiwatch.service.TMDBService;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/content")
public class ContentController {
    @Autowired
    private ContentService contentService;

    @Autowired
    private TMDBService tmdbService;

    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    private ReviewServiceImpl reviewService;

    @GetMapping("/search")
    public String search(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }
        List<ContentSummaryDto> contentList = contentService.getContentSummary();
        Collections.shuffle(contentList);
        model.addAttribute("contentList", contentList);
        return "content/searchPage";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("contentId") int contentId, Model model, @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        int memberId = 0;
        if(userDetails != null) { //로그인 했음
            Member user = memberService.findByEmail(userDetails.getUsername());
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("user", user);
            memberId = user.getMemberId();

            //유저가 좋아요를 눌렀는지, 봤어요를 눌렀는지 확인해줘야 함
            model.addAttribute("heartUrl", "/image/icon/icon-heart-red.png");
            model.addAttribute("eyeUrl", "/image/icon/icon-eye.png");
        } else {
            model.addAttribute("isLoggedIn", false);
        }


        ContentDto contentInfo = contentService.getContentDetail(contentId);
        if(contentInfo == null) { //content가 현재 DB에 없음
            contentInfo = contentService.getContentInfo(contentId);
        }
        model.addAttribute("contentInfo", contentInfo);

        //전체 review
        List<ReviewDto> reviewList = reviewService.getReviewsByContent(memberId, contentId);
        model.addAttribute("reviewList", reviewList);

        int total = 0;
        for(ReviewDto review : reviewList) {
            total += Integer.parseInt(review.getRate());
        }
        double average = 0;
        if(userDetails != null) {
            //사용자 review
            List<ReviewDto> memberReview = reviewService.getReviewsByContentAndUser(memberId, contentId);
            if(!memberReview.isEmpty()) {
                model.addAttribute("memberReview", memberReview.get(0));
                total += Integer.parseInt(memberReview.get(0).getRate());
                average = (double) total / (reviewList.size() + 1);
            } else {
                model.addAttribute("memberReview", null);
            }
        } else {
            model.addAttribute("memberReview", null);
            average = (double) total / reviewList.size();
        }
        model.addAttribute("average", String.format("%.1f", average));

        return "content/detailPage";
    }

    @PostMapping("/likeUpdate")
    @ResponseBody
    public ResponseEntity<?> updateContentLike(@RequestParam int contentId, @RequestParam int memberId,
                                            @RequestParam boolean isLiked,
                                            Principal principal) {
        System.out.println("contentId는 : " + contentId);
        System.out.println("memberId는 : " + memberId);
        System.out.println("isLike는" + isLiked);

        return ResponseEntity.ok().build();
    }
    @PostMapping("/watchedUpdate")
    @ResponseBody
    public ResponseEntity<?> updateContentWatched(@RequestParam int contentId, @RequestParam int memberId,
                                               @RequestParam boolean isWatched,
                                               Principal principal) {
        System.out.println("contentId는 : " + contentId);
        System.out.println("memberId는 : " + memberId);
        System.out.println("isWatched는 : " + isWatched);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/recommend")
    public String recommendContent(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, HttpSession session) throws Exception {

        if(userDetails != null) {
            model.addAttribute("isLoggedIn", true);
            Member loginUser = memberService.findByEmail(userDetails.getUsername());
            List<ForMeContentDto> recommendList = tmdbService.getForMeContents(50, loginUser.getPreferredGenres());

            Collections.shuffle(recommendList);
            model.addAttribute("recommendList", recommendList);
        }
        return "content/recommendPage";
    }

    //API 에서 콘텐츠 받아와서 DB에 데이터 넣어둠
    //자정마다 실행됨!
    @Scheduled(cron = "0 0 0 * * *")
    public String insert(Model model) {
        contentService.insertNewContent(50, 50);
        return "content/searchPage";
    }

    //uri로도 콘텐츠 insert 가능
    @GetMapping("/insert")
    public String insertByURI(Model model) {
        contentService.insertNewContent(50, 50);
        return "content/searchPage";
    }
}
