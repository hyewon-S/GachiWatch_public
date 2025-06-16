package ssd.springcooler.gachiwatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssd.springcooler.gachiwatch.dao.CrewChatDao;
import ssd.springcooler.gachiwatch.dao.CrewDao;
import ssd.springcooler.gachiwatch.dao.CrewJoinWaitingDao;
import ssd.springcooler.gachiwatch.domain.*;
import ssd.springcooler.gachiwatch.dto.CrewDto;
import ssd.springcooler.gachiwatch.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CrewServiceImpl implements CrewFacade {
    private static final int PAGE_SIZE = 4;

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

    @Autowired
    private CrewJoinWaitingRepository crewJoinWaitingRepository;


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
        Platform platformEnum = Platform.valueOf(platform);
        return crewRepository.findByPlatform(platformEnum, pageable);
    }
/*
    public Page<Crew> getAllCrewsByMemberId(int memberId, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return joinedCrewRepository.findByMemberMemberId(memberId, pageable);
    }
*/
    public List<Crew> getCrewList(Platform platform) {
        return crewRepository.findAll();
    }

    public List<Crew> getCrewListByMemberId(Integer memberId) {
        List<JoinedCrew> joinedCrewList = joinedCrewRepository.findByMemberId(memberId);
        List<Crew> crewList = joinedCrewList.stream()
                .map(JoinedCrew::getCrew)  // 각 JoinedCrew에서 Crew 추출
                .collect(Collectors.toList());
        return crewList;
    }

    public Page<Crew> getCrewListByMemberId(int memberId, int page) {
        //return crewMemberRepository.findAllByCrewId(memberId);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<JoinedCrew> joinedCrewsPage = joinedCrewRepository.findByMemberMemberId(memberId, pageable);
        return joinedCrewsPage.map(JoinedCrew::getCrew);
    }


    @Transactional
    public CrewDto getCrewWithChat(Long crewId) {
        Optional<Crew> crew = crewRepository.findByCrewId(crewId);

        List<CrewChat> chatList = crewChatRepository.findByCrewIdJPQL(crewId);
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

    @Transactional
    public Crew createCrew(Crew crew, Member captain) {
        crew.setCaptain(captain);
        crewRepository.save(crew);

        JoinedCrew joinedCrew = new JoinedCrew(captain, crew, true);
        //joinedCrewRepository.save(joinedCrew);
        joinedCrewRepository.insertJoinedCrew(crew.getCrewId(), captain.getMemberId(), true);
        return crew;
    }
    public Crew updateCrew(Crew crew) {
        return crewDao.updateCrew(crew);
    }
    public boolean deleteCrew(Crew crew) {
        return crewDao.deleteCrew(crew);
    }

    public boolean makeApplication(Long crewId, Member member) {
        System.out.println(crewId + " , " + member.getMemberId());
        CrewJoinWaiting crewJoinWaiting = new CrewJoinWaiting(crewId, member.getMemberId());
        crewJoinWaitingRepository.save(crewJoinWaiting);
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
        return crewChatRepository.findByCrewIdJPQL(crewId);
    }
/*
    @Transactional
    @Override
    public boolean insertCrewChat(Long crewId, String chat, Date date, Integer memberId) {
        CrewChat crewChat = new CrewChat(crewId, date, chat, memberId);
        crewChatRepository.save(crewChat);
        return true;

    }
*/
    @Transactional
    public boolean insertCrewChat(Crew crew, String chat, Date date, Member member){
        CrewChat crewChat = new CrewChat(crew, date, chat, member);
        crewChatRepository.save(crewChat);
        return true;
    }

    public boolean insertCrewChat(CrewChat crewChat){
        crewChatRepository.save(crewChat);
        return true;
    }

    @Override
    public List<Member> getCrewMembers(Long crewId) {
        return crewMemberRepository.findAllByCrewId(crewId);
    }

}