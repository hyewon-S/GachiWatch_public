package ssd.springcooler.gachiwatch.dao;

import ssd.springcooler.gachiwatch.domain.ReviewReport;

import java.util.List;

public interface ReviewReportDao {
    // 1. 리뷰 신고 등록
    void createReport(ReviewReport report);

    // 2. 리뷰 신고 수정
    void updateReport(ReviewReport report);

    // 3. 리뷰 신고 삭제
    void deleteReport(int reportId);

    // 4. 리뷰 신고 조회
    ReviewReport getReportById(int reportId);

    // 5. 사용자의 모든 리뷰 신고 조회
    List<ReviewReport> getReportsByUserId(int userId);
}
