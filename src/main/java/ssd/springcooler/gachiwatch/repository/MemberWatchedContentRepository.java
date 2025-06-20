package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssd.springcooler.gachiwatch.domain.WatchedContent;

import java.util.List;

public interface MemberWatchedContentRepository extends JpaRepository<WatchedContent, Integer> {
    boolean existsByMemberIdAndContentId(int memberId, int contentId);

    void deleteByMemberIdAndContentId(int memberId, int contentId);

    List<WatchedContent> findByMemberId(int memberId);
}
