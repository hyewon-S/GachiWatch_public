package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class CrewJoinWaiting {
    @Id
    @OneToOne
    @JoinColumn(name = "crew_id")
    Crew crew;

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

    public Crew getCrew() {
        return crew;
    }
    public void setCrew(Crew crew) {
        this.crew = crew;
    }
    public List<Member> getWaitings() {
        return waitings;
    }
    public void setWaitings(ArrayList<Member> waitings) {
        this.waitings = waitings;
    }
}
