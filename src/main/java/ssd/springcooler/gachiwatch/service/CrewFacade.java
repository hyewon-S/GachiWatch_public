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

    List<Member> getMembers(Long crewId);

    Crew createCrew(Crew crew, Member captain);
    Crew updateCrew(Crew crew);
    boolean deleteCrew(Crew crew);

    boolean makeApplication(Long crewId, Member member);
    boolean denyMember(Long crewId, Integer memberId);
    boolean acceptMember(Long crewId, Integer memberId);

    boolean reportMember(Long crewId, Member member, String reason);
    boolean kickMember(Long crewId, Member member);

    List<CrewChat> getCrewChat(Long crewId);
    boolean insertCrewChat(Crew crew, String chat, Date date, Member member);
    List<Member> getCrewMembers(Long crewId);
}