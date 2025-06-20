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
import org.springframework.web.client.RestTemplate;
import ssd.springcooler.gachiwatch.dao.ContentDao;
import ssd.springcooler.gachiwatch.domain.Content;
import ssd.springcooler.gachiwatch.domain.LikedContent;
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
import java.util.*;


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

    private final RestTemplate restTemplate = new RestTemplate();

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

            String reviewHeart = reviewService.checkReviewHeart(memberId);
            model.addAttribute("review_heartUrl", reviewHeart);

        } else {
            model.addAttribute("isLoggedIn", false);
            model.addAttribute("isReported", false);
        }


        ContentDto contentInfo = contentService.getContentDetail(contentId);
        if(contentInfo == null) { //content가 현재 DB에 없음
            contentInfo = contentService.getContentInfo(contentId);
        }
        model.addAttribute("contentInfo", contentInfo);

        //전체 review
        List<ReviewDto> reviewList = reviewService.getReviewsByContent(memberId, contentId);
        model.addAttribute("reviewList", reviewList);

        for(ReviewDto review : reviewList) {
            boolean isReported = reviewService.checkReported(memberId, review.getReviewId());
            review.setReported(isReported);
        }

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
            List<LikedContent> likedContentList = contentService.getLikedContentsByUserId(loginUser.getMemberId());

            Set<ContentDto> contentSet = new HashSet<>();
            Set<Integer> seenIds = new HashSet<>();
            int count = 0;
            for (LikedContent l: likedContentList) {
                if (count > 2)
                    break;
                System.out.println(l.toString());
                int contentId = l.getContentId();

                if (seenIds.contains(contentId)) continue;
                seenIds.add(contentId);

                List<ContentDto> recommendations = recommendationService.getRecommendations(contentId, 30);

                contentSet.addAll(recommendations);
                count++;
            }

            List<ContentDto> recommendList = new ArrayList<>(contentSet);

            Collections.shuffle(recommendList);
            recommendList = recommendList.subList(0, Math.min(50, recommendList.size()));

            model.addAttribute("recommendList", recommendList);
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

        // type이 null 또는 빈 경우, 아무것도 선택되지 않은 상태이므로 빈 결과 반환
        if (type == null || type.isEmpty()) {
            return Collections.emptyList();
        }

        List<ContentSummaryDto> filtered = all.stream()
                .filter(c -> ott == null || ott.isEmpty() ||  // 필터 없으면 전체 통과
                        c.getOtt().stream().anyMatch(ott::contains)) // 하나라도 포함되면 OK
                .filter(c -> type.contains(c.getContentType().toUpperCase()))
                .toList();
System.out.println(sort);
        return sortContentList(filtered, sort);
    }

    public List<ContentSummaryDto> sortContentList(List<ContentSummaryDto> contentList, String sortType) {

        if (contentList == null || contentList.isEmpty()) return contentList;

        // ❗ 불변 리스트를 변경 가능하게 복사
        List<ContentSummaryDto> sortedList = new ArrayList<>(contentList);

        if ("sortMax".equals(sortType)) {
            sortedList.sort(Comparator.comparingDouble(ContentSummaryDto::getRateAsDouble).reversed());
        } else if ("sortMin".equals(sortType)) {
            sortedList.sort(Comparator.comparingDouble(ContentSummaryDto::getRateAsDouble));
        }

        return sortedList;
    }

    @PostMapping ("/keyword")
    public String keyword(@RequestParam(required = false) String keyword, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }
        if (keyword == null || keyword.trim().isEmpty()) {
            // 예외 처리 or 메시지 전달
            System.out.println("⚠️ 키워드가 비어있습니다.");
            model.addAttribute("err", true);
            model.addAttribute("count", 0);
            return "content/keywordResultPage";
        }
        try {
            List<ContentSummaryDto> contentList = tmdbService.runKeywordSearch(keyword);
            Collections.shuffle(contentList);
            model.addAttribute("contentList", contentList);
            model.addAttribute("keyword", keyword);
            model.addAttribute("count", contentList.size());
        } catch (Exception e) {
            System.out.println("❌ 키워드 검색 실패");
            model.addAttribute("count", 0);
        }
        return "content/keywordResultPage";
    }

    //API 에서 콘텐츠 받아와서 DB에 데이터 넣어둠
    //자정마다 실행됨!
    @Scheduled(cron = "0 0 0 * * *")
    public String insert() {
        contentService.insertNewContent(50, 50);
        try {
            Process process = Runtime.getRuntime().exec("python3 retrain_model.py");
            process.waitFor();
        } catch (Exception e) {
            System.err.println("[ERROR] 모델 재학습 중 예외: " + e.getMessage());
        }

        try {
            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:5000/reload_model", String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("[SUCCESS] Flask 모델 리로드 성공");
            } else {
                System.err.println("[ERROR] Flask 모델 리로드 실패: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Flask 모델 리로드 예외: " + e.getMessage());
        }
        return "content/searchPage";
    }

    //uri로도 콘텐츠 insert 가능
    @GetMapping("/insert")
    public String insertByURI(Model model) {
        contentService.insertNewContent(50, 50);
        return "content/searchPage";
    }
}
