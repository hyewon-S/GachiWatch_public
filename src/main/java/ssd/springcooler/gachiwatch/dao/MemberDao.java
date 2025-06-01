package ssd.springcooler.gachiwatch.dao;

import java.util.List;

import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.dto.*;

public interface MemberDao {
    // 회원가입
//    void insertMember(MemberRegisterDto dto);
    void insertMember(Member member);
//    void insertMemberGenres(Long memberId, List<Long> genreIds);
//    void insertMemberOtts(Long memberId, List<Long> ottIds);

    void insertMemberGenres(int memberId, List<Long> genreIds);

    // 로그인
    LoginDto findByEmailAndPassword(LoginDto loginDto);
    LoginDto findByKakaoEmail(String email);

    // 프로필
    void updateProfile(ProfileUpdateDto dto);
    void updatePassword(Long memberId, String newPassword);
    void updateProfileImage(Long memberId, String imageUrl);

    // OTT / 장르 수정
    void deleteMemberOtts(int memberId);
    void deleteMemberGenres(int memberId);

    // 리뷰
    List<ReviewDto> selectMyReviews(Long memberId);
    void deleteReviewById(Long reviewId);

    // 봤어요 콘텐츠
    List<ContentSummaryDto> selectWatchedContents(int memberId);
    void deleteWatchedContentById(int contentId);

    List<ReportDto> selectReportsByMe(Long memberId);

    // 신고
//    List<ReportDto> selectReportsByMe(Long memberId);
    List<ReportDto> selectReportsAgainstMe(int memberId);

    // 참여한 가치크루
    List<CrewDto> selectMyCrews(int memberId);

    // 좋아요한 콘텐츠
    List<ContentSummaryDto> selectLikedContents(Long memberId);

    // 회원 탈퇴
    void deleteMemberById(Long memberId);

    String emailCheck(String email);
}
