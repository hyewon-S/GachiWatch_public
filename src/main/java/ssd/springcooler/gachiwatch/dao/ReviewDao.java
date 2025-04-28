package ssd.springcooler.gachiwatch.dao;
import ssd.springcooler.gachiwatch.domain.Review;

public interface ReviewDao {
    // 1. 리뷰 등록
    void insertReview(Review review);

    // 2. 리뷰 수정
    void updateReview(Review review);

    // 3. 리뷰 삭제
    void deleteReview(int reviewId);

    // 4. 리뷰 상세 조회
    Review getReviewById(int reviewId);

    // 5. 특정 콘텐츠에 대한 모든 리뷰 조회
    List<Review> getReviewsByContentId(int contentId);

    // 6. 사용자의 모든 리뷰 조회
    List<Review> getReviewsByUserId(int userId);
}
