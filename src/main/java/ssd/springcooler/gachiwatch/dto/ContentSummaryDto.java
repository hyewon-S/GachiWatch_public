package ssd.springcooler.gachiwatch.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ContentSummaryDto { // 영화정보 간략하게 보여주기
    private int contentId;
    private String title;
    private String thumbnailUrl;
    private double rating;

    public ContentSummaryDto(String title, String thumbnailUrl, double rating) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.rating = rating;
    }
}
