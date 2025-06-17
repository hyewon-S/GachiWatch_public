package ssd.springcooler.gachiwatch.dto;
import lombok.Getter;
import lombok.Setter;
import ssd.springcooler.gachiwatch.domain.Genre;

import java.util.List;

@Getter
@Setter
public class EmailNotiRequestDto {
    private String nickname;
    private List<Genre> preferredGenres; //String -> 에서 Genre로 변경
    private String email;
}
