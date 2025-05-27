package ssd.springcooler.gachiwatch.dao.mybatis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ssd.springcooler.gachiwatch.dao.MemberDao;
import ssd.springcooler.gachiwatch.dao.mybatis.mapper.MemberMapper;
import ssd.springcooler.gachiwatch.dto.*;

@Repository
public class MyBatisMemberDao implements MemberDao {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public void insertMember(MemberRegisterDto dto) {
        memberMapper.insertMember(dto);
    }

    @Override
    public void insertMemberGenres(int memberId, List<Long> genreIds) {
        //구현 필요
    }

//    @Override
//    public void insertMemberGenres(int memberId, List<Long> genreIds) {
//        memberMapper.insertMemberGenres(memberId, genreIds);
//    }
//
//    @Override
//    public void insertMemberOtts(int memberId, List<Long> ottIds) {
//        memberMapper.insertMemberOtts(memberId, ottIds);
//    }

    @Override
    public LoginDto findByEmailAndPassword(LoginDto loginDto) {
        return memberMapper.findByEmailAndPassword(loginDto);
    }

    @Override
    public LoginDto findByKakaoEmail(String email) {
        return memberMapper.findByKakaoEmail(email);
    }

    @Override
    public void updateProfile(ProfileUpdateDto dto) {
        memberMapper.updateProfile(dto);
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
    public void deleteMemberOtts(int memberId) {
        memberMapper.deleteMemberOtts(memberId);
    }

    @Override
    public void deleteMemberGenres(int memberId) {
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
    public List<ContentSummaryDto> selectWatchedContents(int memberId) {
        return memberMapper.selectWatchedContents(memberId);
    }

    @Override
    public void deleteWatchedContentById(int contentId) {
        memberMapper.deleteWatchedContentById(contentId);
    }

    @Override
    public List<ReportDto> selectReportsByMe(Long memberId) {
        return memberMapper.selectReportsByMe(memberId);
    }

    @Override
    public List<ReportDto> selectReportsAgainstMe(int memberId) { return memberMapper.selectReportsAgainstMe(memberId); }

    @Override
    public List<CrewDto> selectMyCrews(int memberId) {
        return memberMapper.selectMyCrews(memberId);
    }

    @Override
    public List<ContentSummaryDto> selectLikedContents(Long memberId) { return memberMapper.selectLikedContents(memberId); }

    @Override
    public void deleteMemberById(Long memberId) {
        memberMapper.deleteMemberById(memberId);
    }
}

