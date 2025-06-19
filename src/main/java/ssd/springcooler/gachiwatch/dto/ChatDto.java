package ssd.springcooler.gachiwatch.dto;

import lombok.*;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.Member;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatDto implements Serializable {
    private Crew crew;
    private Member member;
    private String chat;
    private Date date;
}
