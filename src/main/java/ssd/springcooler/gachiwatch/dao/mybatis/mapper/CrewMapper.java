package ssd.springcooler.gachiwatch.dao.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;

import java.util.List;

@Mapper
public interface CrewMapper {
   Crew getCrewByCrewId(int id);

    List<Crew> getCrewList();

    List<Member> getMemberByCrewId(int crewId);

    Member getCaptain(int crewId);

    List<Crew> getCrewListByPlatforms(List<Platform> platforms);

    Crew insertCrew(Crew crew);

    Crew updateCrew(Crew crew);

    boolean deleteCrew(Crew crew);

    boolean insertMember(int crewId, Member member);

    boolean deleteMember(int crewId, Member member);
}
