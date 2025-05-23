package ssd.springcooler.gachiwatch.dao;

import java.util.List;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.dto.*;

public interface MemberDao {
    // 회원가입
    void insertMember(MemberRegisterDto dto);
    void insertMemberGenres(Long memberId, List<Long> genreIds);
    void insertMemberOtts(Long memberId, List<Long> ottIds);

    // 로그인
    LoginDto findByEmailAndPassword(LoginDto loginDto);
    LoginDto findByKakaoEmail(String email);

    // 프로필
    void updateProfile(Long memberId, ProfileUpdateDto dto);
    void updatePassword(Long memberId, String newPassword);
    void updateProfileImage(Long memberId, String imageUrl);

    // OTT / 장르 수정
    void deleteMemberOtts(Long memberId);
    void deleteMemberGenres(Long memberId);

    // 리뷰
    List<ReviewDto> selectMyReviews(Long memberId);
    void deleteReviewById(Long reviewId);

    // 봤어요 콘텐츠
    List<ContentSummaryDto> selectWatchedContents(Long memberId);
    void deleteWatchedContentById(Long contentId);

    List<ReportDto> selectReportsByMe(Long memberId);

    // 신고
//    List<ReportDto> selectReportsByMe(Long memberId);
    List<ReportDto> selectReportsAgainstMe(Long memberId);

    // 참여한 가치크루
    List<CrewDto> selectMyCrews(Long memberId);

    // 좋아요한 콘텐츠
    List<ContentSummaryDto> selectLikedContents(Long memberId);

    // 회원 탈퇴
    void deleteMemberById(Long memberId);
}
