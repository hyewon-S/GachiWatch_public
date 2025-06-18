package ssd.springcooler.gachiwatch.service;

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

        // String → Platform enum 변환 (예외 잡고 null 필터링)
        List<Platform> platformList = dto.getSubscribedOtts().stream()
                .map(name -> {
                    try {
//                        return Platform.fromDisplayName(String.valueOf(name));
                        return Platform.valueOf(String.valueOf(name)); // ← 여기 수정!
                    } catch (IllegalArgumentException e) {
                        System.out.println("[경고] 올바르지 않은 플랫폼 이름: " + name);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        // String → Genre enum 변환 (예외 처리도 넣어줘)
        List<Genre> genreList = dto.getPreferredGenres().stream()
                .map(name -> {
                    try {
                        return Genre.fromDisplayName(String.valueOf(name));
                    } catch (IllegalArgumentException e) {
                        System.out.println("[경고] 올바르지 않은 장르 이름: " + name);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        // DTO → Entity 변환
        Member member = Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .gender(gender)  // 위에서 변환한 값 사용
                .birthdate(dto.getBirthdate())
                .subscribedOTTs(platformList)
                .preferredGenres(genreList)

//                .subscribedOTTs(dto.getSubscribedOtts() != null ? dto.getSubscribedOtts() : new ArrayList<>())
//                .preferredGenres(dto.getPreferredGenres() != null ? dto.getPreferredGenres() : new ArrayList<>())
                .build();

//        // 플랫폼 매핑
//        platformList.forEach(platform -> {
//            Member_Platform mp = Member_Platform.builder()
//                    .member(member)
//                    .platform(platform)
//                    .build();
//            member.getSubscribedOTTs().add(mp);
//        });
//
//        // 장르 매핑
//        genreList.forEach(genre -> {
//            Member_Genre mg = Member_Genre.builder()
//                    .member(member)
//                    .genre(genre)
//                    .build();
//            member.getPreferredGenres().add(mg);
//        });

        // 비밀번호 암호화
        // member.setPassword(passwordEncoder.encode(member.getPassword()));

        memberRepository.save(member);
    }

    // 이메일 검증
    @Override
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email); // JPA 기준, 혹은 MyBatis면 DAO 호출
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
//        // 1. 프로필 이미지 처리
//        if (profileImage != null && !profileImage.isEmpty()) {
//            System.out.println("파일 저장 시작");
//            String profileImagePath = fileStorageService.store(profileImage);
//            System.out.println("저장된 파일 경로: " + profileImagePath);
//            dto.setProfileImage(profileImagePath);
//        } else {
//            dto.setProfileImage(null); // 변경 안함 처리
//        }

        // 2. 닉네임 비어있으면 null 처리
        if (dto.getNickname() == null || dto.getNickname().isBlank()) {
            dto.setNickname(null);
        }

//        // 3. 비밀번호 처리
//        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
//            if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
//                throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
//            }
//            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
//        } else {
//            dto.setPassword(null); // 변경 안함
//        }

        // 4. DAO 호출
//        memberDao.updateProfile(dto, profileImage);
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

    /** 구독 중인 OTT 조회 */
    @Override
    public List<Platform> getSubscribedOttList(int memberId) {
        return memberMapper.getSubscribedOttList(memberId); // mapper에서 DB 조회
    }

    /** 구독 중인 OTT 수정 */
    @Override
    public void updateSubscribedOtt(int memberId, List<Platform> ottList) {
        // 기존 OTT 삭제
        memberDao.deleteMemberOtts(memberId);

        // 새 OTT 저장 (String → ID 변환 로직이 필요하다면 서비스에서 처리)
        // List<Long> ottIds = convertToOttIds(ottList);
        // memberDao.insertMemberOtts(memberId, ottIds);
    }

    /** 선호 장르 수정 */
    @Override
    public void updatePreferredGenre(int memberId, List<Genre> genreList) {
        // 기존 장르 삭제
        memberDao.deleteMemberGenres(memberId);

        // 새 장르 저장 (String → ID 변환 로직이 필요하다면 서비스에서 처리)
        // List<Long> genreIds = convertToGenreIds(genreList);
        // memberDao.insertMemberGenres(memberId, genreIds);
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

    @Override
    public List<Genre> getPreferredGenres(int memberId) {
        return List.of();
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
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