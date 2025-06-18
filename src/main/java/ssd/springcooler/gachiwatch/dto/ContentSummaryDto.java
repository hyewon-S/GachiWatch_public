package ssd.springcooler.gachiwatch.dto;

import lombok.*;
import ssd.springcooler.gachiwatch.domain.Platform;

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

    public ContentSummaryDto(String title, String thumbnailUrl, String rating, List<String> ott, String contentType) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.rating = rating;
        this.ott = ott;
        this.contentType = contentType;
    }
}
