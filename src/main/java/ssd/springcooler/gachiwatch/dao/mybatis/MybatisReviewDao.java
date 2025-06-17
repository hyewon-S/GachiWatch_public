package ssd.springcooler.gachiwatch.dao.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ssd.springcooler.gachiwatch.dao.ReviewDao;
import ssd.springcooler.gachiwatch.dao.mybatis.mapper.ReviewMapper;
import ssd.springcooler.gachiwatch.domain.Review;

import java.util.List;

@Repository
public class MybatisReviewDao implements ReviewDao {
    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public void insertReview(Review review) {
        reviewMapper.insertReview(review);
    }

    @Override
    public void updateReview(int reviewId, String substance, int rate, String date){
        reviewMapper.updateReview(reviewId, substance, rate, date);
    }

    @Override
    public void deleteReview(int reviewId){
        reviewMapper.deleteReview(reviewId);
    }

    @Override
    public Review getReviewById(int reviewId){
        return reviewMapper.getReviewById(reviewId);
    }

    @Override
    public List<Review> getReviewsByContentId(int contentId){
        return reviewMapper.getReviewsByContentId(contentId);
    }

    @Override
    public List<Review> getReviewsByUserId(int userId){
        return reviewMapper.getReviewsByUserId(userId);
    }

    @Override
    public List<Review> getReviewsByContentIdAndMemberId(int memberId, int contentId) {
        return reviewMapper.getReviewsByContentIdAndMemberId(memberId, contentId);
    }
}
