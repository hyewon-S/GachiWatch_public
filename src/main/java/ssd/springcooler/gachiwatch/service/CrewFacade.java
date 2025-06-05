package ssd.springcooler.gachiwatch.service;

import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.CrewChat;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.dto.MemberDto;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CrewFacade {
    Optional<Crew> getCrew(Long crewId);
    List<Crew> getCrewList();
    //List<Crew> getCrewList(List<Platform> platforms);

    Member getCaptain(Long crewId);
    List<Member> getMembers(Long crewId);

    Crew createCrew(Crew crew);
    Crew updateCrew(Crew crew);
    boolean deleteCrew(Crew crew);

    boolean makeApplication(Long crewId, Member member);
    boolean denyMember(Long crewId, Member member);
    boolean acceptMember(Long crewId, Member member);

    boolean reportMember(Long crewId, Member member, String reason);
    boolean kickMember(Long crewId, Member member);

    List<CrewChat> getCrewChat(Long crewId);
    boolean insertCrewChat(Long crewId, String chat, Date date);

    List<Member> getCrewMembers(Long crewId);
}