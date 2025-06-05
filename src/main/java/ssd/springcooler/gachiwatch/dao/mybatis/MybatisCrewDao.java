package ssd.springcooler.gachiwatch.dao.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ssd.springcooler.gachiwatch.dao.CrewDao;
import ssd.springcooler.gachiwatch.dao.mybatis.mapper.CrewMapper;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;

import java.util.List;

@Repository
public class MybatisCrewDao implements CrewDao {
    @Autowired
    private CrewMapper crewMapper;

    @Override
    public Member getCaptain(int crewId) {
        return crewMapper.getCaptain(crewId);
    }

    @Override
    public List<Member> getMembers(int crewId) {
        return crewMapper.getMemberByCrewId(crewId);
    }

    @Override
    public List<Crew> getCrewList() {
        return crewMapper.getCrewList();
    }

    @Override
    public List<Crew> getCrewList(List<Platform> platforms) {
        return crewMapper.getCrewListByPlatforms(platforms);
    }

    @Override
    public Crew getCrew(int crewId) {
        return crewMapper.getCrewByCrewId(crewId);
    }

    @Override
    public Crew insertCrew(Crew crew) {
        return crewMapper.insertCrew(crew);
    }

    @Override
    public Crew updateCrew(Crew crew) {
        return crewMapper.updateCrew(crew);
    }

    @Override
    public boolean deleteCrew(Crew crew) {
        return crewMapper.deleteCrew(crew);
    }

    @Override
    public boolean insertMember(int crewId, Member member) {
        return crewMapper.insertMember(crewId, member);
    }

    @Override
    public boolean deleteMember(int crewId, Member member) {
        return crewMapper.deleteMember(crewId, member);
    }
}
