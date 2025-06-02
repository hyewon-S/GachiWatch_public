package ssd.springcooler.gachiwatch.dao.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.dto.*;

import java.util.List;

@Mapper
public interface MemberMapper {

    // 회원가입
//    void insertMember(MemberRegisterDto dto);
    void insertMember(Member member);
    void insertMemberGenres(@Param("memberId") int memberId, @Param("genreIds") List<Long> genreIds);
    void insertMemberOtts(@Param("memberId") int memberId, @Param("ottIds") List<Long> ottIds);

    // 로그인
    LoginDto findByEmailAndPassword(LoginDto loginDto);
    LoginDto findByKakaoEmail(String email);

    // 프로필
    void updateProfile(@Param("dto") ProfileUpdateDto dto);
    void updatePassword(@Param("memberId") Long memberId, @Param("newPassword") String newPassword);
    void updateProfileImage(@Param("memberId") Long memberId, @Param("imageUrl") String imageUrl);

    // OTT / 장르 수정
    void deleteMemberOtts(int memberId);
    void deleteMemberGenres(int memberId);

    // 리뷰
    List<ReviewDto> selectMyReviews(Long memberId);
    void deleteReviewById(Long reviewId);

    // 봤어요 콘텐츠
    List<ContentSummaryDto> selectWatchedContents(int memberId);
    void deleteWatchedContentById(int contentId);

    // 신고
//    List<ReportDto> selectReportsByMe(Long memberId);
    List<ReportDto> selectReportsAgainstMe(int memberId);

    // 참여한 가치크루
    List<CrewDto> selectMyCrews(int memberId);

    // 좋아요한 콘텐츠
    List<ContentSummaryDto> selectLikedContents(Long memberId);

    // 회원 탈퇴
    void deleteMemberById(Long memberId);

    List<ReportDto> selectReportsByMe(Long memberId);
}

