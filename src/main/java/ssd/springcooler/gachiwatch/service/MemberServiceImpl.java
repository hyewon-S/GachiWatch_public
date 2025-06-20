package ssd.springcooler.gachiwatch.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ssd.springcooler.gachiwatch.dao.MemberDao;
import ssd.springcooler.gachiwatch.dao.mybatis.mapper.MemberMapper;
import ssd.springcooler.gachiwatch.domain.*;
import ssd.springcooler.gachiwatch.dto.*;
import ssd.springcooler.gachiwatch.repository.*;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor -> 오류가 발생하는 듯 하여 주석 처리해두었습니다.
public class MemberServiceImpl implements MemberService {

    private final MemberDao memberDao;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;
    private final ReviewRepository reviewRepository;

    private final ContentRepository contentRepository;

    private MemberLikedContentRepository memberLikedContentRepository;
    private MemberWatchedContentRepository memberWatchedContentRepository;

    protected MemberServiceImpl(MemberDao memberDao,
                                MemberRepository memberRepository,
                                MemberMapper memberMapper,
                                PasswordEncoder passwordEncoder,
                                FileStorageService fileStorageService,
                                ReviewRepository reviewRepository,
                                ContentRepository contentRepository,
                                MemberLikedContentRepository memberLikedContentRepository,
                                MemberWatchedContentRepository memberWatchedContentRepository) {
        this.memberDao = memberDao;
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
        this.reviewRepository = reviewRepository;
        this.contentRepository = contentRepository;
        this.memberLikedContentRepository = memberLikedContentRepository;
        this.memberWatchedContentRepository = memberWatchedContentRepository;
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

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // DTO → Entity 변환
        Member member = Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(encodedPassword)  // 암호화된 비밀번호 저장
//                .password(dto.getPassword()) // 평문 비밇번호 저장
                .nickname(dto.getNickname())
                .gender(gender)  // 위에서 변환한 값 사용
                .birthdate(dto.getBirthdate())
                .subscribedOtts(platformList)
                .preferredGenres(genreList)
                .build();

        member.setPassword(encodedPassword);

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
        System.out.println("로그인 시도: email=" + loginDto.getEmail() + ", password=" + loginDto.getPassword());
        Member m = memberMapper.findByEmailAndPassword(loginDto);
        System.out.println("조회 결과: " + m);
        return m;
//        return memberMapper.findByEmailAndPassword(loginDto);
    }

    /** 프로필 수정 */
    @Override
    public void updateProfile(ProfileUpdateDto dto) {
        System.out.println("updateProfile 호출됨");

        // 닉네임 비어있으면 null 처리
        if (dto.getNickname() == null || dto.getNickname().isBlank()) {
            dto.setNickname(null);
        }

        // 비밀번호가 비어있지 않으면 암호화 처리
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            String encryptedPw = passwordEncoder.encode(dto.getPassword());
            dto.setPassword(encryptedPw);
        } else {
            dto.setPassword(null); // 비밀번호 변경 안할 때 null로 넘기거나 DAO에서 처리
        }

        memberDao.updateProfile(dto);
    }


    /** 참여한 크루 목록 조회 */
    @Override
    public List<CrewDto> getMyCrews(int memberId) {
        return memberDao.selectMyCrews(memberId);
    }

    @Override
    public List<ContentSummaryDto> getLikedContents(int memberId, List<String> ottList, List<String> typeList) {
        List<Integer> likedContentIds = memberLikedContentRepository.findByMemberId(memberId)
                .stream()
                .map(LikedContent::getContentId)
                .toList();

        System.out.println("✅ [찜 콘텐츠] memberId = " + memberId);
        System.out.println("✅ [찜 콘텐츠] contentId 목록 = " + likedContentIds);

        if (likedContentIds.isEmpty()) {
            System.out.println("⚠️ 찜한 콘텐츠 ID가 없습니다.");
            return List.of();
        }

        List<Content> contents = contentRepository.findAllByContentIdIn(likedContentIds);
        System.out.println("✅ [찜 콘텐츠] DB에서 가져온 콘텐츠 수: " + contents.size());
        for (Content c : contents) {
            System.out.println("▶ 콘텐츠 ID: " + c.getContentId() +
                    ", 플랫폼: " + c.getPlatform() +
                    ", 타입: " + c.getContentType());
        }

        List<ContentSummaryDto> result = contents.stream()
                .filter(c -> {
                    if (ottList == null || ottList.isEmpty()) return true;
                    List<String> contentPlatforms = c.getPlatform().stream()
                            .map(id -> {
                                try {
                                    return Platform.fromPlatformId(id).name(); // ex: "NETFLIX"
                                } catch (IllegalArgumentException e) {
                                    System.out.println("⚠️ 플랫폼 변환 실패: " + id);
                                    return null;
                                }
                            })
                            .filter(name -> name != null)
                            .toList();
                    System.out.println("▶ 콘텐츠 " + c.getContentId() + "의 플랫폼: " + contentPlatforms);
                    return contentPlatforms.stream().anyMatch(ottList::contains);
                })
                .filter(c -> {
                    if (typeList == null || typeList.isEmpty()) return true;
                    String contentType = c.getContentType();
                    boolean match = typeList.stream()
                            .map(String::toUpperCase)
                            .anyMatch(t -> t.equals(contentType.toUpperCase()));
                    if (!match) {
                        System.out.println("❌ 콘텐츠 타입 미일치: " + c.getContentId() + " / 타입: " + contentType);
                    }
                    return match;
                })

                .map(ContentSummaryDto::fromEntity)
                .collect(Collectors.toList());

        System.out.println("✅ [찜 콘텐츠] 최종 반환할 DTO 개수: " + result.size());
        return result;
    }


    @Override
    public List<ContentSummaryDto> getWatchedContents(int memberId, List<String> ottList, List<String> typeList) {
        List<Integer> watchedContentIds = memberWatchedContentRepository.findByMemberId(memberId)
                .stream()
                .map(WatchedContent::getContentId)
                .toList();

        System.out.println("✅ [봤어요 콘텐츠] memberId = " + memberId);
        System.out.println("✅ [봤어요 콘텐츠] contentId 목록 = " + watchedContentIds);

        if (watchedContentIds.isEmpty()) {
            System.out.println("⚠️ 본 콘텐츠 ID가 없습니다.");
            return List.of();
        }

        List<Content> contents = contentRepository.findAllByContentIdIn(watchedContentIds);
        System.out.println("✅ [봤어요 콘텐츠] DB에서 가져온 콘텐츠 수: " + contents.size());
        for (Content c : contents) {
            System.out.println("▶ 콘텐츠 ID: " + c.getContentId() +
                    ", 플랫폼: " + c.getPlatform() +
                    ", 타입: " + c.getContentType());
        }

        List<ContentSummaryDto> result = contents.stream()
                .filter(c -> {
                    if (ottList == null || ottList.isEmpty()) return true;
                    List<String> contentPlatforms = c.getPlatform().stream()
                            .map(id -> {
                                try {
                                    return Platform.fromPlatformId(id).name(); // ex: "NETFLIX"
                                } catch (IllegalArgumentException e) {
                                    System.out.println("⚠️ 플랫폼 변환 실패: " + id);
                                    return null;
                                }
                            })
                            .filter(name -> name != null)
                            .toList();
                    System.out.println("▶ 콘텐츠 " + c.getContentId() + "의 플랫폼: " + contentPlatforms);
                    return contentPlatforms.stream().anyMatch(ottList::contains);
                })
                .filter(c -> {
                    if (typeList == null || typeList.isEmpty()) return true;
                    String contentType = c.getContentType();
                    boolean match = typeList.stream()
                            .map(String::toUpperCase)
                            .anyMatch(t -> t.equals(contentType.toUpperCase()));
                    if (!match) {
                        System.out.println("❌ 콘텐츠 타입 미일치: " + c.getContentId() + " / 타입: " + contentType);
                    }
                    return match;
                })

                .map(ContentSummaryDto::fromEntity)
                .collect(Collectors.toList());

        System.out.println("✅ [봤어요 콘텐츠] 최종 반환할 DTO 개수: " + result.size());
        return result;
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


    @Override
    public List<MemberReviewDto> findMyReviews(int memberId, String sort) {
        List<Review> reviews = "desc".equalsIgnoreCase(sort) ?
                reviewRepository.findByMember_MemberIdOrderByScoreDesc(memberId) :
                reviewRepository.findByMember_MemberIdOrderByScoreAsc(memberId);

        return reviews.stream()
                .map(MemberReviewDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteMyReviews(int memberId, List<Integer> reviewIds) {
        // 해당 리뷰가 본인 리뷰인지 검증하고 삭제
        List<Review> toDelete = reviewRepository.findAllById(reviewIds)
                .stream()
                .filter(r -> r.getMember().getMemberId() == memberId)
                .collect(Collectors.toList());

        reviewRepository.deleteAll(toDelete);
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

    @Transactional
    @Override
    public void deleteMemberByEmail(String email) {
        Optional<Member> memberOpt = memberRepository.findByEmail(email);

        memberOpt.ifPresent(member -> {
            member.getLikedContents().clear();
            member.getWatchedContents().clear();
            member.getJoinedCrews().clear();
            memberRepository.delete(member);
        });
    }

//    @Override
//    public boolean deleteMember(int memberId) {
//        //구현 필요
//        return false;
//    }

    @Override
    public Member getMember(int memberId) {
        //return memberRepository.findById(memberId).get();
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (!optionalMember.isPresent()) {
            throw new NoSuchElementException("회원이 존재하지 않거나 탈퇴한 사용자입니다. ID: " + memberId);
        }
        return optionalMember.get();
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }
}