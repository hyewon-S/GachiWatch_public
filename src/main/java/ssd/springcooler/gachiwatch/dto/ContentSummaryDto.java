package ssd.springcooler.gachiwatch.dto;

import lombok.*;
        import ssd.springcooler.gachiwatch.domain.Content;
import ssd.springcooler.gachiwatch.domain.Platform;

import java.util.List;
import java.util.stream.Collectors;

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

    // 문자열을 double로 변환 (예외 처리 포함)
    public double getRateAsDouble() {
        try {
            return Double.parseDouble(rating);
        } catch (NumberFormatException | NullPointerException e) {
            return 0.0; // 잘못된 값이면 0으로 간주
        }
    }

    public static ContentSummaryDto fromEntity(Content content) {
        return ContentSummaryDto.builder()
                .contentId(content.getContentId())
                .title(content.getTitle())
                .thumbnailUrl(content.getImgUrl())
                .rating(content.getRate() == null ? null : String.valueOf(content.getRate()))
                .ott(
                        content.getPlatform() == null ? List.of() :
                                content.getPlatform().stream()
                                        .map(String::valueOf) // 필요시 Platform enum 변환 로직 추가 가능
                                        .collect(Collectors.toList())
                )
                .contentType(content.getContentType())
                .build();
    }
}

