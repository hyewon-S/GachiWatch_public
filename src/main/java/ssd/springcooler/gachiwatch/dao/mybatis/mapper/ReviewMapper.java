package ssd.springcooler.gachiwatch.dao.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import ssd.springcooler.gachiwatch.domain.Review;

import java.util.List;

@Mapper
public interface ReviewMapper {

    void insertReview(Review review);

    void updateReview(int reviewId, String substance, int rate, String date);

    void deleteReview(int reviewId);

    Review getReviewById(int reviewId);

    List<Review> getReviewsByContentId(int contentId);

    List<Review> getReviewsByUserId(int userId);

    List<Review> getReviewsByContentIdAndMemberId(int memberId, int contentId);
}
