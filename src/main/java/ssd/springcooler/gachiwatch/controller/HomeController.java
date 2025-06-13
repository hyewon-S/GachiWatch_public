package ssd.springcooler.gachiwatch.controller;
//import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.Member;

import org.springframework.ui.Model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

//    @GetMapping("/member_home")
//    public String memberHome() {
//        return "home/member_home";
//    }
//@GetMapping("/member_home")
//public String memberHome(Model model) {
//    model.addText("user"); // 혹은 로그인한 사용자 넣어도 됨
//    model.addText("crews"); // 빈 리스트
//    model.addText("movieimages");
//    return "home/member_home";
//}

    @GetMapping("/member_home")
    public String memberHome(Model model, HttpSession session) {
        Member user = (Member) session.getAttribute("user");
        model.addAttribute("user", user);  // ← 이게 핵심
        model.addAttribute("crews"); // 빈 리스트
        model.addAttribute("movieimages");
        return "home/member_home";
    }


}

//@GetMapping("/home")
//public String memberHome(Model model, HttpSession session) {
//    // 예시: 세션에서 user 가져오기
//    Member user = (Member) session.getAttribute("user");
//    model.addAttribute("user", user);
//
//    // 예시: 크루 리스트, 영화 이미지 리스트 등 더미 데이터 넣어주기 또는 실제 서비스 호출
//    List<Crew> crews = crewService.getCrewsByUser(user); // 예시 서비스
//    model.addAttribute("crews", crews);
//
//    List<String> movieimages = movieService.getTrendingMovieImages(); // 예시 서비스
//    model.addAttribute("movieimages", movieimages);
//
//    return "home/member_home";
//}
//

