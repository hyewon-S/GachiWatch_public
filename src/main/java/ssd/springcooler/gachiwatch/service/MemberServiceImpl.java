package ssd.springcooler.gachiwatch.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ssd.springcooler.gachiwatch.dao.MemberDao;
import ssd.springcooler.gachiwatch.dao.mybatis.mapper.MemberMapper;
import ssd.springcooler.gachiwatch.domain.*;
import ssd.springcooler.gachiwatch.dto.*;
import ssd.springcooler.gachiwatch.repository.MemberRepository;

import java.util.List;
import java.util.Objects;

@Service
//@RequiredArgsConstructor -> 오류가 발생하는 듯 하여 주석 처리해두었습니다.
public class MemberServiceImpl implements MemberService {

    private final MemberDao memberDao;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    protected MemberServiceImpl(MemberDao memberDao, MemberRepository memberRepository, MemberMapper memberMapper, PasswordEncoder passwordEncoder, FileStorageService fileStorageService) {
        this.memberDao = memberDao;
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public void register(MemberRegisterDto dto) {

        // Gender 변환 안전하게
        Gender gender;
        try {
            gender = Gender.valueOf(dto.getGender()); // "FEMALE", "MALE", "NO_INFO" 이런 이름일 때
        } catch (IllegalArgumentException e) {
            // 문자열이 숫자인지 체크해서 숫자면 fromCode 호출, 아니면 NO_INFO로 기본 처리
            String genderStr = dto.getGender();
            if (genderStr != null && genderStr.matches("\\d+")) {
                gender = Gender.fromCode(Integer.parseInt(genderStr));
            } else {
                gender = Gender.NO_INFO;  // 알 수 없는 값은 NO_INFO로 처리
            }
        }

        List<Platform> platformList = dto.getSubscribedOtts();
        List<Genre> genreList = dto.getPreferredGenres();

        // DTO → Entity 변환
        Member member = Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .gender(gender)  // 위에서 변환한 값 사용
                .birthdate(dto.getBirthdate())
                .subscribedOtts(platformList)
                .preferredGenres(genreList)
                .build();

        System.out.println("DTO 장르 목록: " + dto.getPreferredGenres());

        memberRepository.save(member);
    }

    // 이메일 검증
    @Override
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email); // JPA 기준, 혹은 MyBatis면 DAO 호출
    }

    // 닉네임 검증
    public boolean isNicknameDuplicated(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    /** 로그인 처리 */
    @Override
    public Member login(LoginDto loginDto) {
        return memberMapper.findByEmailAndPassword(loginDto);
    }

    /** 프로필 수정 */
    @Override
    public void updateProfile(ProfileUpdateDto dto) {
        System.out.println("updateProfile 호출됨");

        // 닉네임 비어있으면 null 처리
        if (dto.getNickname() == null || dto.getNickname().isBlank()) {
            dto.setNickname(null);
        }

        memberDao.updateProfile(dto);
    }

    /** 참여한 크루 목록 조회 */
    @Override
    public List<CrewDto> getMyCrews(int memberId) {
        return memberDao.selectMyCrews(memberId);
    }

    /** '찜했어요' 콘텐츠 조회 */
    @Override
    public List<ContentSummaryDto> getLikedContents(int memberId) {
        //구현 필요
        return null;
    }

    /** '봤어요' 콘텐츠 조회 */
    @Override
    public List<ContentSummaryDto> getWatchedContents(int memberId) {
        return memberDao.selectWatchedContents(memberId);
    }

    /** '봤어요' 콘텐츠 수정 */
    @Override
    public void deleteWatchedContent(int contentId) {
        memberDao.deleteWatchedContentById(contentId);
    }

    @Transactional
    public List<Platform> findMyOttList(Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 없음"));
        return member.getSubscribedOtts();
    }

    @Transactional
    public void updateMyOttList(Integer memberId, List<Platform> newOttList) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 없음"));
        member.getSubscribedOtts().clear();
        member.getSubscribedOtts().addAll(newOttList);
        // 트랜잭션 안에서 자동 반영됨
    }




    // 선호 장르 조회
    @Transactional
    @Override
    public List<Genre> findMyGenreList(Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        return member.getPreferredGenres();
    }

    // 선호 장르 수정
    @Transactional
    @Override
    public void updateMyGenreList(int memberId, List<Genre> genreList) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        member.setPreferredGenres(genreList); // 기존 리스트 clear 후 새로 추가
    }


    /** 내가 신고당한 내역 조회 */
    @Override
    public List<ReportDto> getReports(int memberId) {
        return memberDao.selectReportsAgainstMe(memberId);
    }

    @Override
    public boolean deleteMember(int memberId) {
        //구현 필요
        return false;
    }

    @Override
    public Member getMember(int memberId) {
        return memberRepository.findById(memberId).get();
    }

//    @Override
//    public List<Genre> getPreferredGenres(int memberId) {
//        return List.of();
//    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }
}