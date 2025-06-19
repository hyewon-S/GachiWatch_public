package ssd.springcooler.gachiwatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssd.springcooler.gachiwatch.domain.*;
import ssd.springcooler.gachiwatch.dto.CrewDto;
import ssd.springcooler.gachiwatch.repository.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CrewServiceImpl implements CrewFacade {
    private static final int PAGE_SIZE = 4;

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

    @Autowired
    private MemberServiceImpl memberService;


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
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<JoinedCrew> joinedCrewsPage = joinedCrewRepository.findByMemberMemberId(memberId, pageable);
        return joinedCrewsPage.map(JoinedCrew::getCrew);
    }


    @Transactional
    public CrewDto getCrewWithChat(Long crewId) {
        Optional<Crew> crew = crewRepository.findByCrewId(crewId);

        List<CrewChat> chatList = crewChatRepository.findByCrewIdOrderByChatDateAsc(crewId);

        System.out.println("CrewServiceImple getCrewWithChat test " + chatList);

        return new CrewDto(crew, chatList);
    }

    public List<Member> getMembers(Long crewId) {
        //return crewDao.getMembers(crewId);
        List<JoinedCrew> joinedCrewList = joinedCrewRepository.findAll();
        List<Member> crewList = joinedCrewList.stream()
                .map(JoinedCrew::getMember)  // 각 JoinedCrew에서 Crew 추출
                .collect(Collectors.toList());
        return crewList;
    }

    public List<CrewJoinWaiting> getWaitingList(Long crewId) {
        List<CrewJoinWaiting> crewJoinWaitingList = crewJoinWaitingRepository.findByCrewId(crewId);
        //List<Member> memberList = crewJoinWaitingList.stream()
       //         .map(CrewJoinWaiting::getCrewId);
        return crewJoinWaitingList;
    }

    @Transactional
    public Crew createCrew(Crew crew, Member captain) {
        crew.setCaptain(captain);
        crewRepository.save(crew);

        JoinedCrew joinedCrew = new JoinedCrew(captain, crew, true);
        joinedCrewRepository.insertJoinedCrew(crew.getCrewId(), captain.getMemberId(), true);
        return crew;
    }
    @Transactional
    public Crew updateCrew(Crew crew) {
        return crewRepository.save(crew);
    }

    @Override
    public boolean deleteCrew(Crew crew) {
        crewRepository.delete(crew);
        //더 구현 필요
        return false;
    }

    public boolean makeApplication(Long crewId, Member member) {
        System.out.println(crewId + " , " + member.getMemberId());
        CrewJoinWaiting crewJoinWaiting = new CrewJoinWaiting(crewId, member.getMemberId());
        crewJoinWaitingRepository.save(crewJoinWaiting);
        return true;
    }

    @Transactional
    public boolean denyMember(Long crewId, Integer memberId) {
        crewJoinWaitingRepository.deleteByCrewIdAndUserId(crewId, memberId);
        return true;
    }

    @Transactional
    public boolean acceptMember(Long crewId, Integer memberId) {
        joinedCrewRepository.insertJoinedCrew(crewId, memberId, false);
        crewJoinWaitingRepository.deleteByCrewIdAndUserId(crewId, memberId);

        return true;
    }

    public boolean reportMember(Long crewId, Member member, String reason) {
        //신고내용 db에 추가
        return true;
    }
    @Transactional
    public boolean kickMember(Long crewId, Integer memberId) {
        int deletedCount = joinedCrewRepository.deleteByCrewIdAndMemberId(crewId, memberId);
        if (deletedCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<CrewChat> getCrewChat(Long crewId) {
        return crewChatRepository.findByCrewIdOrderByChatDateAsc(crewId);
    }

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

    public List<Member> getCrewMembersByCrewId(Long crewId) {
        List<JoinedCrew> joinedCrewList = joinedCrewRepository.findByCrewId(crewId);
        List<Member> memberList = joinedCrewList.stream()
                .map(JoinedCrew::getMember)
                .collect(Collectors.toList());

        return memberList;
    }

    @Transactional
    public boolean deleteMemberByMemberId(Long crewId, Integer memberId) {
        int deletedCount = joinedCrewRepository.deleteByCrewIdAndMemberId(crewId, memberId);
        if (deletedCount > 0) {
            return true;
        } else {
            return false;
        }
    }
}