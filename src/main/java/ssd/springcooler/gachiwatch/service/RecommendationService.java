package ssd.springcooler.gachiwatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ssd.springcooler.gachiwatch.dto.ContentDto;

import java.util.stream.Collectors;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {
    @Autowired
    private ContentService contentService;

    private RestTemplate restTemplate = new RestTemplate();
    private String pythonUrl = "http://localhost:5000/recommend";

    public List<ContentDto> getRecommendations(int contentId, int topN) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(pythonUrl)
                .queryParam("content_id", contentId)
                .queryParam("top_n", topN)
                .build(false)
                .toUri();

        System.out.println("uri = " + uri);

        Map<String, Object> body = restTemplate.getForObject(uri, Map.class);
        if (body == null || !body.containsKey("recommendations")) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> recommendations = (List<Map<String, Object>>) body.get("recommendations");
        List<Integer> recommendedIds = recommendations.stream()
                .map(map -> (Integer) map.get("content_id"))
                .collect(Collectors.toList());

        // contentRepository에서 id로 Content 엔티티 조회 후 반환
        return contentService.getContentsByRecommendation(recommendations);
    }
}
