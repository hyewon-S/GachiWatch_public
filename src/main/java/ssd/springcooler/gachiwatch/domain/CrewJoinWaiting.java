package ssd.springcooler.gachiwatch.domain;

import java.util.ArrayList;

public class CrewJoinWaiting {
    Crew crew;
    ArrayList<Member> waitings = new ArrayList<Member>();

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
    public ArrayList<Member> getWaitings() {
        return waitings;
    }
    public void setWaitings(ArrayList<Member> waitings) {
        this.waitings = waitings;
    }
}
