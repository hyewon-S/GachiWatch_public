package ssd.springcooler.gachiwatch.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CrewDto {
    private String crew_id;
    private String platform;
    private String payment;
    private String pay_date;
}
