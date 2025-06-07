package ssd.springcooler.gachiwatch.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ssd.springcooler.gachiwatch.service.ContentService;

@Controller
@RequestMapping("/content")
public class ContentController {
    @Autowired
    private ContentService contentService;

    @GetMapping("/search")
    public String search(Model model, HttpSession session) {
        Object user = session.getAttribute("user");

        if(user != null) { //로그인 여부 boolean 에 세팅
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }
        model.addAttribute("contentList", contentService.getContentSummary());
        return "content/searchPage";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("contentId") int contentId, Model model, HttpSession session) {
        Object user = session.getAttribute("user");

        if(user != null) { //로그인 여부 boolean 에 세팅
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }
        model.addAttribute("contentInfo", contentService.getContentDetail(contentId));
        return "content/detailPage"; //여기서 review 쪽으로 redirect 한번 해줄것
    }

    //API 에서 콘텐츠 받아와서 DB에 데이터 넣어둠
    //자정마다 실행됨!
    @Scheduled(cron = "0 0 0 * * *")
    public String insert(Model model) {
        contentService.insertNewContent(50, 50);
        return "content/searchPage";
    }
}
