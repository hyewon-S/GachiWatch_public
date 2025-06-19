package ssd.springcooler.gachiwatch.controller;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrewCommand {
    Long crewId;
    Member captain;
    @NotBlank(message = "크루 이름은 필수입니다.")
    String crewName;
    @Size(max = 500, message = "설명은 최대 500자입니다.")
    String crewDesc;
    @NotNull(message = "플랫폼을 선택해주세요.")
    Platform platform;
    @Min(value = 1000, message = "최소 결제 금액은 1000원입니다.")
    int payment;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date payDate;
    @Min(value = 1, message = "최소 인원은 1명입니다.")
    int maxMember;
    String account;
}
