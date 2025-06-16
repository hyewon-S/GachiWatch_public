package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class JoinedCrewId implements Serializable {
    private Integer memberId;
    private Long crewId;

    public JoinedCrewId(Integer memberId, Long crewId) {
        this.memberId = memberId;
        this.crewId = crewId;
    }
}
