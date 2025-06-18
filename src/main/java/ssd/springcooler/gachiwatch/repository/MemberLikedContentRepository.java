package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssd.springcooler.gachiwatch.domain.LikedContent;

import java.util.List;

public interface MemberLikedContentRepository extends JpaRepository<LikedContent, Integer> {
    boolean existsByMemberIdAndContentId(int memberId, int contentId);

    void deleteByMemberIdAndContentId(int memberId, int contentId);

    @Query("SELECT l FROM LikedContent l WHERE l.memberId = :memberId")
    List<LikedContent> findContentsByMemberId(@Param("memberId") Integer memberId);
}
