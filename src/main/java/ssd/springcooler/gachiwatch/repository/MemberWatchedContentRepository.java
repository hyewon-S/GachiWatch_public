package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssd.springcooler.gachiwatch.domain.WatchedContent;

public interface MemberWatchedContentRepository extends JpaRepository<WatchedContent, Integer> {
    boolean existsByMemberIdAndContentId(int memberId, int contentId);

    void deleteByMemberIdAndContentId(int memberId, int contentId);

    WatchedContent findByMemberId(int memberId);
}
