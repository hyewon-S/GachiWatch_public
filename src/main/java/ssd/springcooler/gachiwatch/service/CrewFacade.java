package ssd.springcooler.gachiwatch.service;

import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.Member;

import java.util.Date;
import java.util.List;

public interface CrewFacade {
    Crew getCrew(int crewId);
    List<Crew> getCrewList();
    //List<Crew> getCrewList(List<Platform> platforms);

    Member getCaptain(int crewId);
    List<Member> getMembers(int crewId);

    Crew createCrew(Crew crew);
    Crew updateCrew(Crew crew);
    boolean deleteCrew(Crew crew);

    boolean makeApplication(int crewId, Member member);
    boolean denyMember(int crewId, Member member);
    boolean acceptMember(int crewId, Member member);

    boolean reportMember(int crewId, Member member, String reason);
    boolean kickMember(int crewId, Member member);

    List<String> getCrewChat(int crewId);
    boolean insertCrewChat(int crewId, String chat, Date date);
}