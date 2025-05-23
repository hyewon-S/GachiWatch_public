package ssd.springcooler.gachiwatch.dao.mybatis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ssd.springcooler.gachiwatch.dto.LoginDto;
import ssd.springcooler.gachiwatch.dto.MemberRegisterDto;

@Repository
public class MyBatisMemberDao implements MemberDao {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public void insertMember(MemberRegisterDto dto) {
        memberMapper.insertMember(dto);
    }

    @Override
    public void insertMemberGenres(Long memberId, List<Long> genreIds) {
        memberMapper.insertMemberGenres(memberId, genreIds);
    }

    @Override
    public void insertMemberOtts(Long memberId, List<Long> ottIds) {
        memberMapper.insertMemberOtts(memberId, ottIds);
    }

    @Override
    public LoginDto findByEmailAndPassword(LoginDto loginDto) {
        return memberMapper.findByEmailAndPassword(loginDto);
    }

    @Override
    public LoginDto findByKakaoEmail(String email) {
        return memberMapper.findByKakaoEmail(email);
    }

    @Override
    public void updateProfile(Long memberId, ProfileUpdateDto dto) {
        memberMapper.updateProfile(memberId, dto);
    }

    @Override
    public void updatePassword(Long memberId, String newPassword) {
        memberMapper.updatePassword(memberId, newPassword);
    }

    @Override
    public void updateProfileImage(Long memberId, String imageUrl) {
        memberMapper.updateProfileImage(memberId, imageUrl);
    }

    @Override
    public void deleteMemberOtts(Long memberId) {
        memberMapper.deleteMemberOtts(memberId);
    }

    @Override
    public void deleteMemberGenres(Long memberId) {
        memberMapper.deleteMemberGenres(memberId);
    }

    @Override
    public List<ReviewDto> selectMyReviews(Long memberId) {
        return memberMapper.selectMyReviews(memberId);
    }

    @Override
    public void deleteReviewById(Long reviewId) {
        memberMapper.deleteReviewById(reviewId);
    }

    @Override
    public List<ContentDto> selectWatchedContents(Long memberId) {
        return memberMapper.selectWatchedContents(memberId);
    }

    @Override
    public void deleteWatchedContentById(Long contentId) {
        memberMapper.deleteWatchedContentById(contentId);
    }

    @Override
    public List<ReportDto> selectReportsByMe(Long memberId) {
        return memberMapper.selectReportsByMe(memberId);
    }

    @Override
    public List<ReportReceivedDto> selectReportsAgainstMe(Long memberId) {
        return memberMapper.selectReportsAgainstMe(memberId);
    }

    @Override
    public List<CrewDto> selectMyCrews(Long memberId) {
        return memberMapper.selectMyCrews(memberId);
    }

    @Override
    public List<ContentDto> selectLikedContents(Long memberId) {
        return memberMapper.selectLikedContents(memberId);
    }

    @Override
    public void deleteMemberById(Long memberId) {
        memberMapper.deleteMemberById(memberId);
    }
}

