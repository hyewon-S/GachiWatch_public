package ssd.springcooler.gachiwatch.dao.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import ssd.springcooler.gachiwatch.dto.*;

import java.util.List;

@Mapper
public interface MemberMapper {

    // 회원가입
    void insertMember(MemberRegisterDto dto);
    void insertMemberGenres(@Param("memberId") Long memberId, @Param("genreIds") List<Long> genreIds);
    void insertMemberOtts(@Param("memberId") Long memberId, @Param("ottIds") List<Long> ottIds);

    // 로그인
    LoginDto findByEmailAndPassword(LoginDto loginDto);
    LoginDto findByKakaoEmail(String email);

    // 프로필
    void updateProfile(@Param("memberId") Long memberId, @Param("dto") ProfileUpdateDto dto);
    void updatePassword(@Param("memberId") Long memberId, @Param("newPassword") String newPassword);
    void updateProfileImage(@Param("memberId") Long memberId, @Param("imageUrl") String imageUrl);

    // OTT / 장르 수정
    void deleteMemberOtts(Long memberId);
    void deleteMemberGenres(Long memberId);

    // 리뷰
    List<ReviewDto> selectMyReviews(Long memberId);
    void deleteReviewById(Long reviewId);

    // 봤어요 콘텐츠
    List<ContentSummaryDto> selectWatchedContents(Long memberId);
    void deleteWatchedContentById(Long contentId);

    // 신고
//    List<ReportDto> selectReportsByMe(Long memberId);
    List<ReportDto> selectReportsAgainstMe(Long memberId);

    // 참여한 가치크루
    List<CrewDto> selectMyCrews(Long memberId);

    // 좋아요한 콘텐츠
    List<ContentSummaryDto> selectLikedContents(Long memberId);

    // 회원 탈퇴
    void deleteMemberById(Long memberId);

    List<ReportDto> selectReportsByMe(Long memberId);
}

