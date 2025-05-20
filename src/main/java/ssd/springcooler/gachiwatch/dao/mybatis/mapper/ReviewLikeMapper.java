package ssd.springcooler.gachiwatch.dao.mybatis.mapper;

public interface ReviewLikeMapper {
    void addReviewLike (int reviewId, int memberId, int contentId);

    void updateReviewLike(int reviewId, int memberId, int contentId);

    void deleteReviewLike(int reviewId, int memberId, int contentId);

    int getLikeCountByIds(int reviewId, int memberId, int contentId);
}
