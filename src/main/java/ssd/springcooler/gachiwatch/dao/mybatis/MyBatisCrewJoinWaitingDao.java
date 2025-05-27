package ssd.springcooler.gachiwatch.dao.mybatis;

import org.springframework.stereotype.Repository;
import ssd.springcooler.gachiwatch.dao.CrewJoinWaitingDao;
import ssd.springcooler.gachiwatch.domain.Member;

import java.util.List;

@Repository
public class MyBatisCrewJoinWaitingDao  implements CrewJoinWaitingDao {
    @Override
    public List<Member> getWaiting(int crewId) {
        return null;
    }

    @Override
    public boolean insertMember(int crewId, Member member) {
        return false;
    }

    @Override
    public boolean deleteMember(int crewId, Member member) {
        return false;
    }
}
