package ssd.springcooler.gachiwatch.controller;

import jakarta.persistence.*;
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
    Member captain;
    String crewName;
    String crewDesc;
    Platform platform;
    int payment;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date payDate;
    int maxMember;
    String account;
}
