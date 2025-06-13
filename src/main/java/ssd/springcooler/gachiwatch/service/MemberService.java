package ssd.springcooler.gachiwatch.service;

import ssd.springcooler.gachiwatch.domain.Genre;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.dto.*;

import java.util.List;

public interface MemberService {

//    static String emailCheck(String email) {
//        return email;
//    }

    /**
     * 회원가입 처리
     * @param dto 회원가입 정보 DTO
     */
    void register(MemberRegisterDto dto);


    /**
     * 로그인 처리
     *
     * @param dto 로그인 요청 DTO
     * @return
     */
    Member login(LoginDto loginDto);

    /*
     * 프로필 정보 수정
     * @param memberId 회원 ID
     * @param dto 수정할 프로필 정보 DTO
     */
    void updateProfile(ProfileUpdateDto dto);

    /**
     * 내가 참여 중인 가치크루 목록 조회
     * @param memberId 회원 ID
     * @return 참여 중인 Crew 목록
     */
    List<CrewDto> getMyCrews(int memberId);

    /**
     * '봤어요' 콘텐츠 목록 조회
     *
     * @param memberId 회원 ID
     * @return 내가 본 콘텐츠 목록
     */
    List<ContentSummaryDto> getLikedContents(int memberId);


    /**
     * 프로필 정보 수정
     * @param memberId 회원 ID
     * @param dto 수정할 프로필 정보 DTO
     */
//    void updateProfile(Long memberId, ProfileUpdateDto dto);

//    List<CrewDto> getMyCrews(Long memberId);

    /**
     * '봤어요' 콘텐츠 목록 조회
     *
     * @param memberId 회원 ID
     * @return 내가 본 콘텐츠 목록
     */
    List<ContentSummaryDto> getWatchedContents(int memberId);

    /**
     * '봤어요' 콘텐츠 개별 삭제
     */
    void deleteWatchedContent(int contentId);

//    void deleteWatchedContent(int memberId, int contentId);


    /**
     * 구독 중인 OTT 목록 수정
     * @param memberId 회원 ID
     * @param ottList OTT ID 목록
     */
    void updateSubscribedOtt(int memberId, List<Platform> ottList);

    /**
     * 선호 장르 목록 수정
     * @param memberId 회원 ID
     * @param genreList 장르 ID 목록
     */
    void updatePreferredGenre(int memberId, List<Genre> genreList);

    /**
     * 내가 신고당한 내역 조회
     * @param memberId 회원 ID
     * @return 신고받은 내역 목록
     */
    List<ReportDto> getReports(int memberId);

    boolean deleteMember(int memberId);

    Member getMember(int memberId);

//    LoginDto login(String email, String password);
}
