package ssd.springcooler.gachiwatch.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ContentSummaryDto { // 영화정보 간략하게 보여주기
    private int contentId;
    private String title;
    private String thumbnailUrl;
    private String rating;
    private List<String> ott;
    private String contentType;
// 문자열을 double로 변환 (예외 처리 포함)
    public double getRateAsDouble() {
        try {
            return Double.parseDouble(rating);
        } catch (NumberFormatException | NullPointerException e) {
            return 0.0; // 잘못된 값이면 0으로 간주
        }
    }
}
