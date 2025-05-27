package ssd.springcooler.gachiwatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssd.springcooler.gachiwatch.dao.CrewChatDao;
import ssd.springcooler.gachiwatch.dao.CrewDao;
import ssd.springcooler.gachiwatch.dao.CrewJoinWaitingDao;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.Member;

import java.util.Date;
import java.util.List;

@Service
public class CrewServiceImpl implements CrewFacade {
    @Autowired
    private CrewDao crewDao;
    @Autowired
    private CrewJoinWaitingDao waitingDao;
    @Autowired
    private CrewChatDao chatDao;

    public Crew getCrew(int crewId) {
        return crewDao.getCrew(crewId);
    }
    public List<Crew> getCrewList() {
        return crewDao.getCrewList();
    }
    /*
    public List<Crew> getCrewList(List<Platform> platforms) {
        return crewDao.getCrewList(platforms);
    }
    */

    public Member getCaptain(int crewId) {
        return crewDao.getCaptain(crewId);
    }
    public List<Member> getMembers(int crewId) {
        return crewDao.getMembers(crewId);
    }

    public Crew createCrew(Crew crew) {
        return crewDao.insertCrew(crew);
    }
    public Crew updateCrew(Crew crew) {
        return crewDao.updateCrew(crew);
    }
    public boolean deleteCrew(Crew crew) {
        return crewDao.deleteCrew(crew);
    }

    public boolean makeApplication(int crewId, Member member) {
        return waitingDao.insertMember(crewId, member);
    }
    public boolean denyMember(int crewId, Member member) {
        return waitingDao.deleteMember(crewId, member);
    }

    public boolean acceptMember(int crewId, Member member) {
        crewDao.insertMember(crewId, member);
        waitingDao.deleteMember(crewId, member);
        return true;
    }

    public boolean reportMember(int crewId, Member member, String reason) {
        //신고내용 db에 추가
        return true;
    }
    public boolean kickMember(int crewId, Member member) {
        return crewDao.deleteMember(crewId, member);
    }

    public List<String> getCrewChat(int crewId) {
        return chatDao.getChat(crewId);
    }
    public boolean insertCrewChat(int crewId, String chat, Date date){
        return chatDao.insertChat(crewId, chat, date);
    }
}