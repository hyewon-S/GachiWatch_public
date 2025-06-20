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

        try {
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
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            // 404 에러 발생 시 처리: 모델 재학습/재로딩 API 호출
            System.out.println("[WARN] 추천 콘텐츠 없음, Flask 모델 재로딩 시도 중...");

            try {
                String reloadUrl = "http://localhost:5000/reload_model";
                Map<String, Object> reloadResponse = restTemplate.getForObject(reloadUrl, Map.class);
                System.out.println("[INFO] Flask 모델 재로딩 응답: " + reloadResponse);
            } catch (Exception ex) {
                System.err.println("[ERROR] Flask 모델 재로딩 요청 실패: " + ex.getMessage());
            }

            // 재로딩 후 빈 리스트 반환하거나, 필요 시 재요청 로직 구현 가능
            return Collections.emptyList();
        } catch (Exception e) {
            // 그 외 에러 처리
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
