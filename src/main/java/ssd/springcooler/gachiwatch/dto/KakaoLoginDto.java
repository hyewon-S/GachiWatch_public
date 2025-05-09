package ssd.springcooler.gachiwatch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class KakaoLoginDto {

    @NotBlank
    private String kakaoAccessToken;
}

