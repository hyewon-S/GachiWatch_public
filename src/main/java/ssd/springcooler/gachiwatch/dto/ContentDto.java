package ssd.springcooler.gachiwatch.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ContentDto {
    //필드 추가 예정...
    private int contentId;
    private String title;
    private String thumbnailUrl;
    private double rating;
    private String casts;
    private String intro;
}
