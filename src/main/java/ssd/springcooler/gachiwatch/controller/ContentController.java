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
import ssd.springcooler.gachiwatch.domain.Content;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.dto.ContentDto;
import ssd.springcooler.gachiwatch.dto.ContentSummaryDto;
import ssd.springcooler.gachiwatch.dto.ForMeContentDto;
import ssd.springcooler.gachiwatch.dto.ReviewDto;
import ssd.springcooler.gachiwatch.security.CustomUserDetails;
import ssd.springcooler.gachiwatch.service.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;


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

    @Autowired
    private RecommendationService recommendationService;

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
            String heartUrl = contentService.checkHeart(contentId, memberId);
            model.addAttribute("heartUrl", heartUrl);

            String eyeUrl = contentService.checkWatched(contentId, memberId);
            model.addAttribute("eyeUrl", eyeUrl);
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
                                            @RequestParam boolean isLiked ) {
        contentService.updateLiked(contentId, memberId, isLiked);

        return ResponseEntity.ok().build();
    }
    @PostMapping("/watchedUpdate")
    @ResponseBody
    public ResponseEntity<?> updateContentWatched(@RequestParam int contentId, @RequestParam int memberId,
                                               @RequestParam boolean isWatched ) {
        contentService.updateWatched(contentId, memberId, isWatched);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/recommend")
    public String recommendContent(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, HttpSession session) throws Exception {

        if(userDetails != null) {
            model.addAttribute("isLoggedIn", true);
            Member loginUser = memberService.findByEmail(userDetails.getUsername());

            // 예시: 사용자가 가장 최근에 본 콘텐츠 제목으로 추천 요청
            // (실제론 좋아요 누른 콘텐츠나 최근 본 콘텐츠 ID -> 제목 매핑 필요)
            
            String recentContentTitle = "에이리언";
            Integer contentId = 1017163;
            List<ContentDto> recommendList = recommendationService.getRecommendations(contentId, 20);

            // 추천 API에서 반환하는 추천 콘텐츠 목록은 Map 형태(title, score 포함)
            model.addAttribute("recommendList", recommendList);
            /*
            List<ForMeContentDto> recommendList = tmdbService.getForMeContents(50, loginUser.getPreferredGenres());

            Collections.shuffle(recommendList);
            model.addAttribute("recommendList", recommendList);

             */
        }
        return "content/recommendPage";
    }

    @ResponseBody
    @GetMapping("/filter")
    public List<ContentSummaryDto> filterContents(
            @RequestParam(required = false) List<String> ott,
            @RequestParam(required = false) List<String> type,
            @RequestParam(defaultValue = "default") String sort
    ) {
        List<ContentSummaryDto> all = contentService.getContentSummary();
        List<Platform> ottList = List.of(Platform.NETFLIX, Platform.WAVVE, Platform.WATCHA, Platform.APPLE, Platform.AMAZON, Platform.DISNEY);
        List<String> typeList = List.of("TV", "MOVIE");

//ott가 비었으면 추가 동작 하지 말기
// type이 null 또는 빈 경우, 아무것도 선택되지 않은 상태이므로 빈 결과 반환
        if (type == null || type.isEmpty()) {
            return Collections.emptyList();
        }

        List<ContentSummaryDto> filtered = all.stream()
                .filter(c -> ott == null || ott.isEmpty() ||  // 필터 없으면 전체 통과
                        c.getOtt().stream().anyMatch(ott::contains)) // 하나라도 포함되면 OK
                .filter(c -> type.contains(c.getContentType().toUpperCase()))
                .toList();

        return filtered;
//        Stream<ContentSummaryDto> stream = all.stream();
//
//        if (ott != null && !ott.isEmpty()) {
//            stream = stream.filter(c -> ott.contains(c.getOtt()));
//        }
//
//        if (type != null && !type.isEmpty()) {
//            stream = stream.filter(c -> type.contains(c.getContentType()));
//        }
//
//        // 정렬
//        switch (sort) {
//            case "rating":
//                stream = stream.sorted(Comparator.comparing(Content::getRating).reversed());
//                break;
//            case "recent":
//                stream = stream.sorted(Comparator.comparing(Content::getReleaseDate).reversed());
//                break;
//            case "default":
//            default:
//                // 아무 정렬도 하지 않음 (초기 순서 유지)
//                break;
//        }

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
