package ssd.springcooler.gachiwatch.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReviewDto {
    private String reviewId;
    private String date;
    private String substance;
    private String thumbnailUrl;
    private String content_title;
    private String rate;
}
