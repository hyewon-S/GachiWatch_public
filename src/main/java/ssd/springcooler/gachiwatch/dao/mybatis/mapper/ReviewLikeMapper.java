package ssd.springcooler.gachiwatch.dao.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewLikeMapper {
    void addReviewLike (@Param("reviewId") int reviewId, @Param("memberId") int memberId, @Param("contentId") int contentId);

    void updateReviewLike( @Param("reviewId") int reviewId, @Param("memberId") int memberId, @Param("contentId") int contentId);

    void deleteReviewLike( @Param("reviewId") int reviewId, @Param("memberId") int memberId, @Param("contentId") int contentId);

    int getLikeCountByIds( @Param("reviewId") int reviewId, @Param("memberId") int memberId, @Param("contentId") int contentId);
}
