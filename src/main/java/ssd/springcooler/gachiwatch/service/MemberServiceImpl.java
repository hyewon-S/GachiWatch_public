package ssd.springcooler.gachiwatch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssd.springcooler.gachiwatch.dao.MemberDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberDao memberDao;

    /**
     * 회원가입 처리: 회원 기본정보, 선호 장르, 구독 OTT 등록
     */
    @Override
    public void signUp(SignUpDto dto) {
        memberDao.insertMember(dto);
        memberDao.insertMemberGenres(dto.getMemberId(), dto.getGenreIds());
        memberDao.insertMemberOtts(dto.getMemberId(), dto.getOttIds());
    }

    /**
     * 로그인 처리
     */
    @Override
    public MemberDto login(LoginDto dto) {
        return memberDao.findByEmailAndPassword(dto);
    }

    /**
     * 프로필 수정
     */
    @Override
    public void updateProfile(Long memberId, ProfileUpdateDto dto) {
        memberDao.updateProfile(memberId, dto);
    }

    /**
     * 참여한 가치크루 목록 조회
     */
    @Override
    public List<CrewDto> getMyCrews(Long memberId) {
        return memberDao.selectMyCrews(memberId);
    }

    /**
     * '봤어요' 콘텐츠 조회
     */
    @Override
    public List<ContentDto> getWatchedContents(Long memberId) {
        return memberDao.selectWatchedContents(memberId);
    }

    /**
     * '봤어요' 콘텐츠 삭제
     */
    @Override
    public void deleteWatchedContent(Long contentId) {
        memberDao.deleteWatchedContentById(contentId);
    }

    /**
     * 구독 중인 OTT 수정
     */
    @Override
    public void updateSubscribedOtt(Long memberId, List<String> ottList) {
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
    public void updatePreferredGenre(Long memberId, List<String> genreList) {
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
    public List<ReportReceivedDto> getReports(Long memberId) {
        return memberDao.selectReportsAgainstMe(memberId);
    }
}
