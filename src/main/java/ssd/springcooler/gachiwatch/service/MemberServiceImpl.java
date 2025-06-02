package ssd.springcooler.gachiwatch.service;

import org.springframework.stereotype.Service;
import ssd.springcooler.gachiwatch.dao.MemberDao;
import ssd.springcooler.gachiwatch.dao.mybatis.mapper.MemberMapper;
import ssd.springcooler.gachiwatch.domain.Gender;
import ssd.springcooler.gachiwatch.domain.Genre;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.dto.*;
import ssd.springcooler.gachiwatch.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
//@RequiredArgsConstructor -> 오류가 발생하는 듯 하여 주석 처리해두었습니다.
public class MemberServiceImpl implements MemberService {

    private final MemberDao memberDao;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    protected MemberServiceImpl(MemberDao memberDao, MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberDao = memberDao;
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }
//    // 생성자 주입 (요즘 권장하는 방법)
//    public MemberServiceImpl(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원가입 처리: 회원 기본정보, 선호 장르, 구독 OTT 등록
     */
//    @Override
//    public void register(MemberRegisterDto dto) {
//        memberDao.insertMember(dto);
////        memberDao.insertMemberGenres(dto.getMemberId(), dto.getGenreIds());
////        memberDao.insertMemberOtts(dto.getMemberId(), dto.getOttIds());
//    }

//    @Override
//    public void register(MemberRegisterDto dto) {
//        Member member = Member.builder()
//                .name(dto.getName())
//                .gender(Gender.valueOf(dto.getGender()))
//                .birthdate(dto.getBirthdate())
//                .email(dto.getEmail())
//                .password(dto.getPassword()) // 실제론 암호화 필요
//                .nickname(dto.getNickname())
//                .build();
//
//        memberDao.insertMember(member);
//    }
    @Override
    public void register(MemberRegisterDto dto) {

        // Gender 변환 안전하게
        Gender gender;
        try {
            gender = Gender.valueOf(dto.getGender()); // 문자열이 "FEMALE" 이런 Enum 이름일 때
        } catch (IllegalArgumentException e) {
            // 혹은 코드값이라면
            gender = Gender.fromCode(Integer.parseInt(dto.getGender()));
        }

        // DTO → Entity 변환
        Member member = Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .gender(Gender.valueOf(dto.getGender())) // Enum 변환 주의!
                .birthdate(dto.getBirthdate())
                .subscribedOTTs(dto.getSubscribedOtts() != null ? dto.getSubscribedOtts() : new ArrayList<>())
                .preferredGenres(dto.getPreferredGenres() != null ? dto.getPreferredGenres() : new ArrayList<>())
                .build();

        // 비밀번호는 보통 여기서 암호화 해야 함 (예: BCrypt)
        // member.setPassword(passwordEncoder.encode(member.getPassword()));

//        memberDao.insertMember(member); // DB에 저장
        memberRepository.save(member);
    }


    /**
     * 로그인 처리
     *
     * @return
     */
//    @Override
//    public void login(LoginDto dto) {
//        memberDao.findByEmailAndPassword(dto);
//    }
    @Override
    public Member login(LoginDto loginDto) {
        return memberMapper.findByEmailAndPassword(loginDto);
    }


    /**
     * 프로필 수정
     */
    @Override
    public void updateProfile(ProfileUpdateDto dto) {
        memberDao.updateProfile(dto);
    }

    /**
     * 참여한 가치크루 목록 조회
     */
    @Override
    public List<CrewDto> getMyCrews(int memberId) {
        return memberDao.selectMyCrews(memberId);
    }

    @Override
    public List<ContentSummaryDto> getLikedContents(int memberId) {
        //구현 필요
        return null;
    }

    /**
     * '봤어요' 콘텐츠 조회
     */
    @Override
    public List<ContentSummaryDto> getWatchedContents(int memberId) {
        return memberDao.selectWatchedContents(memberId);
    }

    /**
     * '봤어요' 콘텐츠 삭제
     */
    @Override
    public void deleteWatchedContent(int contentId) {
        memberDao.deleteWatchedContentById(contentId);
    }

    /**
     * 구독 중인 OTT 수정
     */
    @Override
    public void updateSubscribedOtt(int memberId, List<Platform> ottList) {
        // 기존 OTT 삭제
        memberDao.deleteMemberOtts(memberId);

        // 새 OTT 저장 (String → ID 변환 로직이 필요하다면 서비스에서 처리)
        // List<Long> ottIds = convertToOttIds(ottList);
        // memberDao.insertMemberOtts(memberId, ottIds);
    }

    /**
     * 선호 장르 수정
     */
    @Override
    public void updatePreferredGenre(int memberId, List<Genre> genreList) {
        // 기존 장르 삭제
        memberDao.deleteMemberGenres(memberId);

        // 새 장르 저장 (String → ID 변환 로직이 필요하다면 서비스에서 처리)
        // List<Long> genreIds = convertToGenreIds(genreList);
        // memberDao.insertMemberGenres(memberId, genreIds);
    }

    /**
     * 내가 신고당한 내역 조회
     */
    @Override
    public List<ReportDto> getReports(int memberId) {
        return memberDao.selectReportsAgainstMe(memberId);
    }

    @Override
    public boolean deleteMember(int memberId) {
        //구현 필요
        return false;
    }
}

//// repository 사용
//package ssd.springcooler.gachiwatch.service;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import ssd.springcooler.gachiwatch.domain.*;
//import ssd.springcooler.gachiwatch.dto.*;
//import ssd.springcooler.gachiwatch.repository.MemberRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Transactional
//public abstract class MemberServiceImpl implements MemberService {
//
//    private final MemberRepository memberRepository;
//
//    public MemberServiceImpl(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
//
//    @Override
//    public void register(MemberRegisterDto dto) {
//        Member member = Member.builder()
//                .name(dto.getName())
//                .email(dto.getEmail())
//                .password(dto.getPassword())  // 나중에 암호화 필수!
//                .nickname(dto.getNickname())
//                .gender(Gender.valueOf(dto.getGender()))
//                .birthdate(dto.getBirthdate())
//                .subscribedOTTs(dto.getSubscribedOtts() != null ? dto.getSubscribedOtts() : new ArrayList<>())
//                .preferredGenres(dto.getPreferredGenres() != null ? dto.getPreferredGenres() : new ArrayList<>())
//                .build();
//
//        memberRepository.save(member);
//    }
//
//    @Override
//    public void login(LoginDto dto) {
//        // 리포지토리에 메서드 추가 필요!
//        Optional<Member> memberOpt = memberRepository.findByEmailAndPassword(dto.getEmail(), dto.getPassword());
//        if (memberOpt.isEmpty()) {
//            throw new RuntimeException("로그인 실패: 이메일 또는 비밀번호가 잘못되었습니다.");
//        }
//        // 로그인 성공 시 처리 로직 추가
//    }
//
//    @Override
//    public void updateProfile(ProfileUpdateDto dto) {
//        Optional<Member> memberOpt = memberRepository.findById(dto.getMemberId());
//        if (memberOpt.isPresent()) {
//            Member member = memberOpt.get();
//            member.setNickname(dto.getNickname());
//            member.setProfileImage(dto.getProfileImage());
//            // 필요시 다른 필드도 업데이트
//            memberRepository.save(member);
//        } else {
//            throw new RuntimeException("회원 정보가 존재하지 않습니다.");
//        }
//    }
//
//    @Override
//    public List<CrewDto> getMyCrews(int memberId) {
//        Optional<Member> memberOpt = memberRepository.findById(memberId);
//        if (memberOpt.isPresent()) {
//            List<Crew> crews = memberOpt.get().getJoinedCrews();
//            // Crew → CrewDto 변환 필요
//            return crews.stream()
//                    .map(crew -> new CrewDto(crew.getCrewId(), crew.getCrewName()))
//                    .toList();
//        }
//        return new ArrayList<>();
//    }
//
//    @Override
//    public List<ContentSummaryDto> getLikedContents(int memberId) {
//        Optional<Member> memberOpt = memberRepository.findById(memberId);
//        if (memberOpt.isPresent()) {
//            List<Content> liked = memberOpt.get().getLikedContents();
//            // Content → ContentSummaryDto 변환 필요
//            return liked.stream()
//                    .map(content -> new ContentSummaryDto(content.getContentId(), content.getTitle()))
//                    .toList();
//        }
//        return new ArrayList<>();
//    }
//
//    @Override
//    public List<ContentSummaryDto> getWatchedContents(int memberId) {
//        Optional<Member> memberOpt = memberRepository.findById(memberId);
//        if (memberOpt.isPresent()) {
//            List<Content> watched = memberOpt.get().getWatchedContents();
//            // Content → ContentSummaryDto 변환 필요
//            return watched.stream()
//                    .map(content -> new ContentSummaryDto(content.getContentId(), content.getTitle()))
//                    .toList();
//        }
//        return new ArrayList<>();
//    }
//
//    @Override
//    public void deleteWatchedContent(int memberId, int contentId) {
//        Optional<Member> memberOpt = memberRepository.findById(memberId);
//        if (memberOpt.isPresent()) {
//            Member member = memberOpt.get();
//            member.getWatchedContents().removeIf(content -> content.getContentId() == contentId);
//            memberRepository.save(member);
//        } else {
//            throw new RuntimeException("회원 정보가 없습니다.");
//        }
//    }
//
//    @Override
//    public void updateSubscribedOtt(int memberId, List<Platform> ottList) {
//        Optional<Member> memberOpt = memberRepository.findById(memberId);
//        if (memberOpt.isPresent()) {
//            Member member = memberOpt.get();
//            member.setSubscribedOTTs(ottList);
//            memberRepository.save(member);
//        } else {
//            throw new RuntimeException("회원 정보가 없습니다.");
//        }
//    }
//
//    @Override
//    public void updatePreferredGenre(int memberId, List<Genre> genreList) {
//        Optional<Member> memberOpt = memberRepository.findById(memberId);
//        if (memberOpt.isPresent()) {
//            Member member = memberOpt.get();
//            member.setPreferredGenres(genreList);
//            memberRepository.save(member);
//        } else {
//            throw new RuntimeException("회원 정보가 없습니다.");
//        }
//    }
//
//    @Override
//    public List<ReportDto> getReports(int memberId) {
//        // Report 엔티티와 repository 필요
//        // 임시로 빈 리스트 반환
//        return new ArrayList<>();
//    }
//
//    @Override
//    public boolean deleteMember(int memberId) {
//        if (memberRepository.existsById((memberId))) {
//            memberRepository.deleteById(memberId);
//            return true;
//        }
//        return false;
//    }
//}
//
