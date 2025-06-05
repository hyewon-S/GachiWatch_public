package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class JoinedCrew {
    @EmbeddedId
    private JoinedCrewId id = new JoinedCrewId();

    @ManyToOne
    @MapsId("memberId")
    private Member member;

    @ManyToOne
    @MapsId("crewId")
    private Crew crew;

    private boolean isLeader;

    public JoinedCrew() {}
    public JoinedCrew(Member member, Crew crew, boolean isLeader) {
        this.member = member;
        this.crew = crew;
        this.isLeader = isLeader;
        this.id = new JoinedCrewId();
    }
}
