package ssd.springcooler.gachiwatch.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class MainController {//홈페이지 첫 메인화면 관련 컨트롤러 HomeController
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        Object user = session.getAttribute("loginUser");

        // 예시 이미지 리스트를 전달. 실제로는 서비스에서 가져오도록 구현 가능
        List<String> movieImages = List.of("movie1.jpg", "movie2.jpg", "movie3.jpg");
        model.addAttribute("movieimages", movieImages);

        if (user != null) {//로그인이 됐으면 회원용 메인 페이지로 이동
            //model.addAttribute("crews", getSampleCrews()); //임의의 Crew값 넣어보기
            model.addAttribute("user", user);
            return "home/member_home"; //member_home.html로 이동(회원)
        } else {
            return "home/home"; // 첫 화면 home.html로 이동 (비회원)
        }

    }

    //Content 실시간 트렌드 관련 코드 추가

    //Content 최신 콘텐츠 관련 코드 추가

    //검색 관련 코드 추가

}
