package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssd.springcooler.gachiwatch.domain.LikedContent;

public interface MemberLikedContentRepository extends JpaRepository<LikedContent, Integer> {
    boolean existsByMemberIdAndContentId(int memberId, int contentId);

    void deleteByMemberIdAndContentId(int memberId, int contentId);
}
