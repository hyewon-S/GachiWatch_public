package ssd.springcooler.gachiwatch.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.dto.CrewInfoDto;
import ssd.springcooler.gachiwatch.dto.ForMeContentDto;
import ssd.springcooler.gachiwatch.dto.LatestMovieDto;
import ssd.springcooler.gachiwatch.dto.TrendingContentDto;
import ssd.springcooler.gachiwatch.repository.JoinedCrewRepository;

import ssd.springcooler.gachiwatch.service.CrewServiceImpl;
import ssd.springcooler.gachiwatch.service.MemberServiceImpl;
import ssd.springcooler.gachiwatch.service.TMDBService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller

public class MainController {//홈페이지 첫 메인화면 관련 컨트롤러
    private final TMDBService tmdbService;
    private final CrewServiceImpl crewService;

    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    private CrewServiceImpl crewService;

    @Autowired
    public MainController(TMDBService tmdbService) {
        this.tmdbService = tmdbService;
        this.crewService = crewService;
    }

    //    @GetMapping("/")
//    public String redirectToHome(Model model, @AuthenticationPrincipal UserDetails userDetails) {
//        if (userDetails != null) {
//            model.addAttribute("user", memberService.findByEmail(userDetails.getUsername()));
//        }
//        return "redirect:/home";
//    }
    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }


    //    @GetMapping("/home")
//    public String home(Model model, HttpSession session, @AuthenticationPrincipal UserDetails userDetails) {
//        Object user = session.getAttribute("user");
//        model.addAttribute("user", user);
//
//        try {
//            List<TrendingContentDto> trending = tmdbService.getTrendingContents(20);
//            model.addAttribute("trendingContents", trending);
//
//            if (user != null) {
//                Member loginUser = (Member) user;
//
//                // Crew 및 추천 콘텐츠 서비스 활성화 시 아래 코드 사용
//                // List<Crew> crews = crewService.getCrewsByUser(loginUser);
//                // model.addAttribute("crews", crews);
//
//                // List<String> movieimages = recommendationService.getRecommendedImages(loginUser);
//                // model.addAttribute("movieimages", movieimages);
//
//                //나를 위한 추천 (통합 및 수정 필요) -> null값이 전달 되는 것 같음
//                 List<ForMeContentDto> formecontents = tmdbService.getForMeContents(20, loginUser.getPreferredGenres() /*수정필요*/);
//                 model.addAttribute("formecontents", formecontents);
//            } else {
//                List<LatestMovieDto> movie = tmdbService.getLatestMovies(20);
//                model.addAttribute("latestMovieContents", movie);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            model.addAttribute("errorMessage", "콘텐츠를 불러오는 중 오류가 발생했습니다.");
//        }
//
//        return "home/home";
//    }
    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            System.out.println("로그인된 유저: " + userDetails.getUsername());
            Member loginUser = memberService.findByEmail(userDetails.getUsername());

            if (loginUser == null) {
                System.out.println("해당 이메일로 회원을 찾을 수 없음!");
            } else {
                System.out.println("회원 정보: " + loginUser.getNickname());
            }
            List<Crew> crews = crewService.getCrewListByMemberId(loginUser.getMemberId());
            model.addAttribute("crews", crews);
            model.addAttribute("user", loginUser);

            try {
                List<TrendingContentDto> trending = tmdbService.getTrendingContents(20);
                model.addAttribute("trendingContents", trending);

                List<ForMeContentDto> formecontents = tmdbService.getForMeContents(20, loginUser.getPreferredGenres());
                model.addAttribute("formecontents", formecontents);

            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "콘텐츠를 불러오는 중 오류가 발생했습니다.");
            }
        } else {
            System.out.println("userDetails가 null임 -> 로그인 안된 상태");
            try {
                List<TrendingContentDto> trending = tmdbService.getTrendingContents(20);
                model.addAttribute("trendingContents", trending);

                List<LatestMovieDto> movie = tmdbService.getLatestMovies(20);
                model.addAttribute("latestMovieContents", movie);
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "콘텐츠를 불러오는 중 오류가 발생했습니다.");
            }
        }

        return "home/home";
    }



    //검색 관련 코드 추가

}