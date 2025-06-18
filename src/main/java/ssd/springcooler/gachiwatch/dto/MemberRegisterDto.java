package ssd.springcooler.gachiwatch.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ssd.springcooler.gachiwatch.domain.Genre;
import ssd.springcooler.gachiwatch.domain.Platform;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder // 생성자, 빌더 패턴은 필수 값만 넣고, 나머진 생략 가능
public class MemberRegisterDto { // 회원가입 요청 DTO
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
    private String gender;
    private LocalDate birthdate;
    private String email;
    private String password;
    private String confirmPassword;
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    // step2에서 추가될 데이터
    private List<Platform> subscribedOtts; // 예시로 스트리밍 서비스 정보를 저장
    private List<Genre> preferredGenres; // 선호 장르
//    private String profileImage;

}
