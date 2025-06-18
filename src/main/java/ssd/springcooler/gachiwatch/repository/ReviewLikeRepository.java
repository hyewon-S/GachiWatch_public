package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssd.springcooler.gachiwatch.domain.ReviewLike;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Integer> {
    void deleteByReviewIdAndMemberId(int reviewId, int memberId);
    boolean existsByMemberId(int memberId);
}
