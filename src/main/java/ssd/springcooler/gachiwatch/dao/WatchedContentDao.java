package ssd.springcooler.gachiwatch.dao;
import ssd.springcooler.gachiwatch.domain.WatchedContent;
import java.util.List;

public interface WatchedContentDao {
    void insertWatchedContent(WatchedContent watchedContent);
    void deleteWatchedContent(int memberId, int contentId);
    WatchedContent getWatchedContent(int memberId, int contentId);
    List<WatchedContent> getWatchedContentsByMember(int memberId);
}
