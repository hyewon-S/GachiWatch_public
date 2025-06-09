package ssd.springcooler.gachiwatch.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ssd.springcooler.gachiwatch.dto.LatestMovieDto;
import ssd.springcooler.gachiwatch.dto.TrendingContentDto;
import ssd.springcooler.gachiwatch.service.TMDBService;

import java.util.List;


@Controller
public class MainController {//홈페이지 첫 메인화면 관련 컨트롤러 HomeController
    private final TMDBService tmdbService;

    @Autowired
    public MainController(TMDBService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        Object user = session.getAttribute("loginUser");

        try {
            List<TrendingContentDto> trending = tmdbService.getTrendingContents(20);
            model.addAttribute("trendingContents", trending);

            List<LatestMovieDto> movie = tmdbService.getLatestMovies(20);
            model.addAttribute("latestMovieContents", movie);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user != null) {//로그인이 됐으면 회원용 메인 페이지로 이동
            //model.addAttribute("crews", getSampleCrews()); //임의의 Crew값 넣어보기
            model.addAttribute("user", user);
            return "home/member_home"; //member_home.html로 이동(회원)
        } else {
            return "home/home"; // 첫 화면 home.html로 이동 (비회원)
        }

    }


    //검색 관련 코드 추가

}
