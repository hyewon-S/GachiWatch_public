package ssd.springcooler.gachiwatch.service;

import ssd.springcooler.gachiwatch.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    List<ReviewDto> getReviewsByUser(int memberId);

    List<ReviewDto> getReviewsByContent(int memberId, int contentId);

    List<ReviewDto> getReviewsByContentAndUser(int memberId, int contentId);

    void deleteReview(int reviewId);

    void insertReview(int contentId, int memberId, String reviewContent, int star);

    void updateReview(int contentId, int memberId, String reviewContent, int star);
}
