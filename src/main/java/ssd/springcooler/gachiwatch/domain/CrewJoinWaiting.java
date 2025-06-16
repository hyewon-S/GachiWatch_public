package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@IdClass(CrewJoinWaitingId.class)
@Table(name="crew_join_waiting_member")
public class CrewJoinWaiting {
    @Id
    @Column(name = "CREW_ID")
    private Long crewId;

    @Id
    @Column(name = "MEMBER_ID")
    private Integer userId;

    public CrewJoinWaiting(Long crewId, Integer memberId) {
        this.crewId = crewId;
        this.userId = memberId;
    }
    /*
    @Id
    @OneToOne
    @MapsId
    @JoinColumn(name = "crew_id")
    Crew crew;

    @Id
    @ManyToMany
    @JoinTable(
            name = "crew_join_waiting_member",
            joinColumns = @JoinColumn(name = "crew_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    List<Member> waitings = new ArrayList<Member>();

    public boolean requestJoin(Member member) {
        return waitings.add(member);
    }

    public boolean acceptJoin(Member member) {
        if (crew.addMember(member))
            return waitings.remove(member);
        else
            return false;
    }

    public boolean refuseJoin(Member member) {
        return waitings.remove(member);
    }

    public void setCrew(Crew crew) {
        this.crew = crew;
    }

    public void setWaitings(ArrayList<Member> waitings) {
        this.waitings = waitings;
    }

     */
}
