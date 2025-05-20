package ssd.springcooler.gachiwatch.dao;

public interface ReviewLikeDao {
    //1. review 좋아요 등록
    void addReviewLike (int reviewId, int memberId, int contentId);

    //2. review 좋아요 수정
    void updateReviewLike(int reviewId, int memberId, int contentId);

    //3. review 좋아요 삭제
    void deleteReviewLike(int reviewId, int memberId, int contentId);

    //4. review 좋아요 개수 카운트해서 반환
    int getLikeCountByIds(int reviewId, int memberId, int contentId);
}
