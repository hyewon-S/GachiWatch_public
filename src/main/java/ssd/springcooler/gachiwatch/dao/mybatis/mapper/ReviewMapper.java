package ssd.springcooler.gachiwatch.dao.mybatis.mapper;

import ssd.springcooler.gachiwatch.domain.Review;

import java.util.List;

public interface ReviewMapper {

    void insertReview(Review review);

    void updateReview(Review review);

    void deleteReview(int reviewId);

    Review getReviewById(int reviewId);

    List<Review> getReviewsByContentId(int contentId);

    List<Review> getReviewsByUserId(int userId);
}
