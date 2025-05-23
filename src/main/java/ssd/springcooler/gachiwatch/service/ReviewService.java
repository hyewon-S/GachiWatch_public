package ssd.springcooler.gachiwatch.service;

import ssd.springcooler.gachiwatch.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    List<ReviewDto> getReviewsByUser(int memberId);

    void deleteReview(int reviewId);
}
