package ssd.springcooler.gachiwatch.dao;
import ssd.springcooler.gachiwatch.domain.SubscribedContent;
import java.util.List;

public interface SubscribedContentDao {
    void insertSubscribedContent(SubscribedContent subscribedContent);
    void deleteSubscribedContent(int memberId, int contentId);
    SubscribedContent getSubscribedContent(int memberId, int contentId);
    List<SubscribedContent> getSubscribedContentsByMember(int memberId);
}
