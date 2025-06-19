package ssd.springcooler.gachiwatch.service;

import jakarta.transaction.Transactional;
import ssd.springcooler.gachiwatch.domain.Genre;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.dto.*;

import java.util.List;

public interface MemberService {

    /** 회원가입 처리 */
    void register(MemberRegisterDto dto);

    /** 이메일 검증 */
    boolean existsByEmail(String email);

    /** 닉네임 검증 */
    boolean isNicknameDuplicated(String nickname);

    /** 로그인 처리 */
    Member login(LoginDto loginDto);

    Member findByEmail(String username);

    /** 프로필 정보 수정 */
    void updateProfile(ProfileUpdateDto dto);

    /** 내가 참여 중인 가치크루 목록 조회 */
    List<CrewDto> getMyCrews(int memberId);

    /** '찜했어요' 콘텐츠 목록 조회 */
    List<ContentSummaryDto> getLikedContents(int memberId);

    /** '봤어요' 콘텐츠 목록 조회 */
    List<ContentSummaryDto> getWatchedContents(int memberId);

    /** '봤어요' 콘텐츠 개별 삭제 */
    void deleteWatchedContent(int contentId);

    /** 리뷰 */
    @Transactional
    void deleteMyReviews(int memberId, List<Integer> reviewIds);
    List<MemberReviewDto> findMyReviews(int memberId, String sort);

    /** 구독 중인 OTT 목록 */
    List<Platform> findMyOttList(Integer memberId); // 구독 OTT 조회
    void updateMyOttList(Integer memberId, List<Platform> newOttList); // 구독 OTT 저장

    /** 선호 장르 목록 */
    List<Genre> findMyGenreList(Integer memberId);
    void updateMyGenreList(int memberId, List<Genre> genreList);


    /** 내가 신고당한 내역 조회*/
    List<ReportDto> getReports(int memberId);

    boolean deleteMember(int memberId);

    Member getMember(int memberId);

}