package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssd.springcooler.gachiwatch.domain.ReviewReport;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, Integer> {
    boolean existsByReviewIdAndMemberId(int reviewId, int memberId);
    void deleteByReviewIdAndMemberId(int reviewId, int memberId);
    int countByReviewId(int reviewId);
    void deleteByReviewId(int reviewId);
}
