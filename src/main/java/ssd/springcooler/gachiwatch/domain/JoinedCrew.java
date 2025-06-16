package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "JOINEDCREWS")
public class JoinedCrew {
    @EmbeddedId
    private JoinedCrewId id = new JoinedCrewId();

    @ManyToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @MapsId("crewId")
    @JoinColumn(name = "crew_id")
    private Crew crew;

    private boolean isLeader;

    public JoinedCrew() {}
    public JoinedCrew(Member member, Crew crew, boolean isLeader) {
        this.member = member;
        this.crew = crew;
        this.isLeader = isLeader;
        this.id = new JoinedCrewId((Integer) member.getMemberId(), crew.getCrewId());
    }
}
