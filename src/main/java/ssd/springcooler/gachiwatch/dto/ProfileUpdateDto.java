package ssd.springcooler.gachiwatch.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProfileUpdateDto {

    private int memberId;
    private String nickname;
    private String password;
    private String passwordConfirm;
//    private String profileImage;

}

