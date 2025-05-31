package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class JoinedCrewId implements Serializable {
    private Long memberId;
    private Long crewId;
}
