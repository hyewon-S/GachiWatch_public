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
import ssd.springcooler.gachiwatch.domain.Genre;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.dto.*;
import ssd.springcooler.gachiwatch.repository.JoinedCrewRepository;

import ssd.springcooler.gachiwatch.service.CrewServiceImpl;
import ssd.springcooler.gachiwatch.service.EmailNotiService;
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

    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    private CrewServiceImpl crewService;

    @Autowired
    private EmailNotiService emailNotiService;

    @Autowired
    public MainController(TMDBService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }

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
                //최신 트렌드
                List<TrendingContentDto> trending = tmdbService.getTrendingContents(20);
                model.addAttribute("trendingContents", trending);

                //나를 위한 추천
                List<ForMeContentDto> formecontents = tmdbService.getForMeContents(20, loginUser.getPreferredGenres());
                model.addAttribute("formecontents", formecontents);

                // === [이메일 추천 발송] ===
                List<Genre> preferredGenres = loginUser.getPreferredGenres();
                String nickname = loginUser.getNickname();
                String email = loginUser.getEmail();

                if (nickname != null && email != null &&
                        preferredGenres != null && !preferredGenres.isEmpty()) {

                    EmailNotiRequestDto emailDto = new EmailNotiRequestDto();
                    emailDto.setMemberId(loginUser.getMemberId());
                    emailDto.setNickname(nickname);
                    emailDto.setEmail(email);
                    emailDto.setPreferredGenres(preferredGenres);

                    emailNotiService.sendRecommendationEmail(emailDto);
                }
                // ==========================


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