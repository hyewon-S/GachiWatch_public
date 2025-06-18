package ssd.springcooler.gachiwatch.service;

import ssd.springcooler.gachiwatch.domain.Review;
import ssd.springcooler.gachiwatch.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    List<ReviewDto> getReviewsByUser(int memberId);

    List<ReviewDto> getReviewsByContent(int memberId, int contentId);

    List<ReviewDto> getReviewsByContentAndUser(int memberId, int contentId);

    void deleteReview(int reviewId);

    void insertReview(int contentId, int memberId, String reviewContent, int star);

    void updateReview(int reviewId, String reviewContent, int rate);

    List<ReviewDto> commonLogicForReview(List<Review> reviewList, int memberId);

    void updateLiked(int reviewId, int memberId, int like, boolean isAdded);

    String checkReviewHeart(int memberId);

    void createReviewReport(int reviewId, int memberId, String substance);

    boolean checkReported(int memberId, String reviewId);

    void deleteReport(int reviewId, int memberId);

    void checkReportedOverFive(int reviewId);
}
