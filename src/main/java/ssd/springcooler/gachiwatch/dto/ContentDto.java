package ssd.springcooler.gachiwatch.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ContentDto {
    private int contentId;
    private String title;
    private String thumbnailUrl;
    private String rating;
    private String uploadDate;
    private String casts;
    private String intro;
    private List<String> genres;
    private List<String> platforms;
}
