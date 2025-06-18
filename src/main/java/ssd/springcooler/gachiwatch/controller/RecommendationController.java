package ssd.springcooler.gachiwatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ssd.springcooler.gachiwatch.service.RecommendationService;

import java.util.Map;

@RestController
public class RecommendationController {
    @Autowired
    private RecommendationService recommendationService;
/*
    @GetMapping("/api/recommend")
    public Map<String, Object> recommend(@RequestParam String title) {
        return recommendationService.getRecommendations(title);
    }*/
}
