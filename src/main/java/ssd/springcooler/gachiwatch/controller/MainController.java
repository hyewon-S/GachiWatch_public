package ssd.springcooler.gachiwatch.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    public MainController(TMDBService tmdbService, CrewServiceImpl crewService) {
        this.tmdbService = tmdbService;
        this.crewService = crewService;
    }

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Object user = session.getAttribute("user");
        model.addAttribute("user", user);

        try {
            List<TrendingContentDto> trending = tmdbService.getTrendingContents(20);
            model.addAttribute("trendingContents", trending);

            if (user != null) {
                Member loginUser = (Member) user;

                // Crew
                // List<Crew> crews = crewService.getCrewsByUser(loginUser);
                // model.addAttribute("crews", crews);

                List<CrewInfoDto> joinedCrews = crewService.getJoinedCrewsByMemberId(loginUser.getUserId());

                // joinedCrews가 null이거나 비어있으면 임의 값으로 대체 (Test값)
                if (joinedCrews == null || joinedCrews.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = sdf.parse("2025-07-07");
                    Date date2 = sdf.parse("2025-07-15");
                    Date date3 = sdf.parse("2025-07-20");

                    joinedCrews = new ArrayList<>();
                    joinedCrews.add(new CrewInfoDto(1L, "테스트 크루 1", Platform.NETFLIX, 12000, date1));
                    joinedCrews.add(new CrewInfoDto(2L, "테스트 크루 2", Platform.WATCHA, 10000, date2));
                    joinedCrews.add(new CrewInfoDto(3L, "테스트 크루 3", Platform.DISNEY, 13000, date3));
                    joinedCrews.add(new CrewInfoDto(4L, "테스트 크루 4", Platform.WAVVE, 12000, date2));
                    joinedCrews.add(new CrewInfoDto(5L, "테스트 크루 5", Platform.APPLE, 11000, date1));
                    joinedCrews.add(new CrewInfoDto(6L, "테스트 크루 6", Platform.AMAZON, 17000, date3));
                }
                model.addAttribute("joinedCrews", joinedCrews);

                //나를 위한 추천 (통합 및 수정 필요) -> null값이 전달 되는 것 같음
                 List<ForMeContentDto> formecontents = tmdbService.getForMeContents(20, loginUser.getPreferredGenres() /*수정필요*/);
                 model.addAttribute("formecontents", formecontents);
            } else {
                List<LatestMovieDto> movie = tmdbService.getLatestMovies(20);
                model.addAttribute("latestMovieContents", movie);
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "콘텐츠를 불러오는 중 오류가 발생했습니다.");
        }

        return "home/home";
    }


    //검색 관련 코드 추가

}
