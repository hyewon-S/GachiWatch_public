package ssd.springcooler.gachiwatch.dao;
import java.util.List;
import ssd.springcooler.gachiwatch.domain.LikedContent;

public interface LikedContentDao {
    void insertLikedContent(LikedContent likedContent);
    void deleteLikedContent(int memberId, int contentId);
    LikedContent getLikedContent(int memberId, int contentId);
    List<LikedContent> getLikedContentsByMember(int memberId);
}
