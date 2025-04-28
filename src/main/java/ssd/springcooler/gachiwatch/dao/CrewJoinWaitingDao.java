package ssd.springcooler.gachiwatch.dao;

import ssd.springcooler.gachiwatch.domain.Member;

import java.util.List;

public interface CrewJoinWaitingDao {
    List<Member> getWaiting(int crewId);
    boolean insertMember(int crewId, Member member);
    boolean deleteMember(int crewId, Member member);
}