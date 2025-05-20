package ssd.springcooler.gachiwatch.dao.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ssd.springcooler.gachiwatch.dao.ReviewLikeDao;
import ssd.springcooler.gachiwatch.dao.mybatis.mapper.ReviewLikeMapper;

@Repository
public class MybatisReviewLikeDao implements ReviewLikeDao {
    @Autowired
    private ReviewLikeMapper reviewLikeMapper;

    @Override
    public void addReviewLike (int reviewId, int memberId, int contentId) {
        reviewLikeMapper.addReviewLike(reviewId, memberId, contentId);
    }

    @Override
    public void updateReviewLike(int reviewId, int memberId, int contentId) {
        reviewLikeMapper.updateReviewLike(reviewId, memberId, contentId);
    }

    @Override
    public void deleteReviewLike(int reviewId, int memberId, int contentId) {
        reviewLikeMapper.deleteReviewLike(reviewId, memberId, contentId);
    }

    @Override
    public int getLikeCountByIds(int reviewId, int memberId, int contentId) {
        return reviewLikeMapper.getLikeCountByIds(reviewId, memberId, contentId);
    }
}
