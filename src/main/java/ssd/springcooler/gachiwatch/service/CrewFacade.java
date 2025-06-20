package ssd.springcooler.gachiwatch.service;

import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.CrewChat;
import ssd.springcooler.gachiwatch.domain.Member;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CrewFacade {
    Optional<Crew> getCrew(Long crewId);
    List<Crew> getCrewList();

    List<Member> getMembers(Long crewId);

    Crew createCrew(Crew crew, Member captain);
    void updateCrew(Crew crew);

    void makeApplication(Long crewId, Member member);
    void denyMember(Long crewId, Integer memberId);
    void acceptMember(Long crewId, Integer memberId);
    void kickMember(Long crewId, Integer memberId);

    List<CrewChat> getCrewChat(Long crewId);
    boolean insertCrewChat(Crew crew, String chat, Date date, Member member);
    List<Member> getCrewMembers(Long crewId);
}