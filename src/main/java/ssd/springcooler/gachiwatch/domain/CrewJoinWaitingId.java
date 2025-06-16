package ssd.springcooler.gachiwatch.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class CrewJoinWaitingId implements Serializable {
    private Long crewId;
    private Integer userId;

    public CrewJoinWaitingId() {}

    public CrewJoinWaitingId(Long crewId, Integer userId) {
        this.crewId = crewId;
        this.userId = userId;
    }

    // 반드시 equals, hashCode 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CrewJoinWaitingId)) return false;
        CrewJoinWaitingId that = (CrewJoinWaitingId) o;
        return Objects.equals(crewId, that.crewId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(crewId, userId);
    }

}
