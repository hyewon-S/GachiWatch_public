package ssd.springcooler.gachiwatch.dao;

import ssd.springcooler.gachiwatch.domain.*;

import java.util.List;

public interface CrewDao {
    Member getCaptain(int crewId);
    List<Member> getMembers(int crewId);
    List<Crew> getCrewList();
    //List<Crew> getCrewList(List<Platform> platforms);

    Crew getCrew(int crewId);
    Crew insertCrew(Crew crew);
    Crew updateCrew(Crew crew);
    boolean deleteCrew(Crew crew);

    boolean insertMember(int crewId, Member member);
    boolean deleteMember(int crewId, Member member);
}
