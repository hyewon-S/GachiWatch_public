package ssd.springcooler.gachiwatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssd.springcooler.gachiwatch.dao.CrewChatDao;
import ssd.springcooler.gachiwatch.dao.CrewDao;
import ssd.springcooler.gachiwatch.dao.CrewJoinWaitingDao;
import ssd.springcooler.gachiwatch.domain.*;
import ssd.springcooler.gachiwatch.dto.CrewDto;
import ssd.springcooler.gachiwatch.repository.CrewChatRepository;
import ssd.springcooler.gachiwatch.repository.CrewMemberRepository;
import ssd.springcooler.gachiwatch.repository.CrewRepository;
import ssd.springcooler.gachiwatch.repository.JoinedCrewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CrewServiceImpl implements CrewFacade {
    private static final int PAGE_SIZE = 6;

    @Autowired
    private CrewDao crewDao;
    @Autowired
    private CrewJoinWaitingDao waitingDao;
    @Autowired
    private CrewChatDao chatDao;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private CrewChatRepository crewChatRepository;

    @Autowired
    private CrewMemberRepository crewMemberRepository;

    @Autowired
    private JoinedCrewRepository joinedCrewRepository;


    public Optional<Crew> getCrew(Long crewId) {
        return crewRepository.findByCrewId(crewId);
    }
    public List<Crew> getCrewList() {
        return crewRepository.findAll();
    }

    public Page<Crew> getAllCrews(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return crewRepository.findAll(pageable);
    }

    public Page<Crew> getAllCrewsByPlatform(String platform, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return crewRepository.findByPlatform(platform, pageable);
    }
    public List<Crew> getCrewList(Platform platform) {
        return crewRepository.findAll();
    }

    public List<JoinedCrew> getCrewListByMemberId(int memberId) {
        //return crewMemberRepository.findAllByCrewId(memberId);
        return joinedCrewRepository.findByMemberMemberId(Long.valueOf(memberId));
    }
    /*
    public List<Crew> getCrewList(List<Platform> platforms) {
        return crewDao.getCrewList(platforms);
    }
    */
/*
    public CrewDto getCrewWithChat(Long crewId) {
        Crew crew = crewRepository.findByCrewId(crewId);
        List<CrewChat> chatList = crewChatRepository.findById_Crew_CrewIdOrderByIdAsc(crewId);

        return new CrewDto(crew, chatList);
    }
*/
    @Transactional
    public CrewDto getCrewWithChat(Long crewId) {
        Optional<Crew> crew = crewRepository.findByCrewId(crewId);

        List<CrewChat> chatList = crewChatRepository.findByIdCrewId(crewId);
        System.out.println("CrewServiceImple getCrewWithChat test " + chatList);

        return new CrewDto(crew, chatList);
    }

    public Member getCaptain(Long crewId) {
        //return crewDao.getCaptain(crewId);
        return null;
    }
    public List<Member> getMembers(Long crewId) {
        //return crewDao.getMembers(crewId);
        return null;
    }

    public Crew createCrew(Crew crew, Member captain) {
        crew.setCaptain(captain);
        crewRepository.save(crew);
        return crew;
    }
    public Crew updateCrew(Crew crew) {
        return crewDao.updateCrew(crew);
    }
    public boolean deleteCrew(Crew crew) {
        return crewDao.deleteCrew(crew);
    }

    public boolean makeApplication(Long crewId, Member member) {
        //return waitingDao.insertMember(crewId, member);
        return true;
    }
    public boolean denyMember(Long crewId, Member member) {
        //return waitingDao.deleteMember(crewId, member);
        return true;
    }

    public boolean acceptMember(Long crewId, Member member) {
        //crewDao.insertMember(crewId, member);
        //waitingDao.deleteMember(crewId, member);
        return true;
    }

    public boolean reportMember(Long crewId, Member member, String reason) {
        //신고내용 db에 추가
        return true;
    }
    public boolean kickMember(Long crewId, Member member) {
        //return crewDao.deleteMember(crewId, member);
        return true;
    }

    public List<CrewChat> getCrewChat(Long crewId) {
        return crewChatRepository.findByIdCrewId(crewId);
    }

    @Override
    public boolean insertCrewChat(Long crewId, String chat, Date date) {
        return false;
    }

    public boolean insertCrewChat(Crew crew, String chat, Date date, Member member){
        //return chatDao.insertChat(crewId, chat, date);
        CrewChat crewChat = new CrewChat(crew, date, chat, member);
        crewChatRepository.save(crewChat);
        return true;
    }

    public boolean insertCrewChat(CrewChat crewChat){
        //return chatDao.insertChat(crewId, chat, date);
        crewChatRepository.save(crewChat);
        return true;
    }

    @Override
    public List<Member> getCrewMembers(Long crewId) {
        return crewMemberRepository.findAllByCrewId(crewId);
    }

}