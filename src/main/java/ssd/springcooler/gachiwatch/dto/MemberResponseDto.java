package ssd.springcooler.gachiwatch.dto;

import lombok.*;
import ssd.springcooler.gachiwatch.domain.Gender;
import ssd.springcooler.gachiwatch.domain.Genre;
import ssd.springcooler.gachiwatch.domain.Platform;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MemberResponseDto {

    private Long memberId;
    private String name;
    private String email;
    private String nickname;
    private Gender gender;
    private LocalDate birthDate;
    private String profileImageUrl;
    private List<Platform> subscribedOtts;
    private List<Genre> preferredGenres;
    private List<ReviewDto> reviews;
    //ContentSummaryDto 따로 만들어서 간략 정보만 보여주는게 좋을듯
    private List<ContentDto> likedContents;
    private List<CrewDto> joinedCrews;
}

