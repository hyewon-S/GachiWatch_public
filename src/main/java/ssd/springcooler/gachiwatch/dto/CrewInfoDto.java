package ssd.springcooler.gachiwatch.dto;

import lombok.Getter;
import ssd.springcooler.gachiwatch.domain.Platform;

import java.util.Date;

@Getter
public class CrewInfoDto { //메인 페이지용 CrewInfo
    private Long crewId;
    private String crewName;
    private String platform;
    private int payment;
    private String payDate;

    public CrewInfoDto(Long crewId, String crewName, Platform platform, int payment, Date payDate) {
        this.crewId = crewId;
        this.crewName = crewName;
        this.platform = platform != null ? platform.name() : null;
        this.payment = payment;
        this.payDate = payDate != null ? payDate.toString() : null;
    }

}
