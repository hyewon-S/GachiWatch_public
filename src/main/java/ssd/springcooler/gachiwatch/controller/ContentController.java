package ssd.springcooler.gachiwatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ssd.springcooler.gachiwatch.service.ContentService;

@Controller
@RequestMapping("/content")
public class ContentController {
    @Autowired
    private ContentService contentService;

    @GetMapping("/search")
    public String search(Model model) {
        model.addAttribute("contentList", contentService.getContentSummary());
        return "content/searchPage";
    }

    //API 에서 콘텐츠 받아와서 DB에 데이터 넣어둠
    //자정마다 실행됨!
    @Scheduled(cron = "0 0 0 * * *")
    public String insert(Model model) {
        contentService.insertNewContent(50, 50);
        return "content/searchPage";
    }
}
