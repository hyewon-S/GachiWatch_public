package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ssd.springcooler.gachiwatch.domain.JoinedCrew;
import ssd.springcooler.gachiwatch.domain.JoinedCrewId;

import java.util.List;
import java.util.Optional;

public interface JoinedCrewRepository extends JpaRepository<JoinedCrew, JoinedCrewId> {
    //List<JoinedCrew> findByMemberMemberId(Long memberId);
    Page<JoinedCrew> findByMemberMemberId(int memberId, Pageable pageable);

    @Query("SELECT jc FROM JoinedCrew jc WHERE jc.crew.id = :crewId AND jc.member.id = :memberId")
    Optional<JoinedCrew> findByCrewIdAndMemberId(@Param("crewId") Long crewId, @Param("memberId") Integer memberId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO joinedcrews (crew_id, member_id, is_leader) VALUES (:crewId, :memberId, :isLeader)", nativeQuery = true)
    void insertJoinedCrew(@Param("crewId") Long crewId,
                          @Param("memberId") Integer memberId,
                          @Param("isLeader") boolean isLeader);

    @Query("SELECT jc FROM JoinedCrew jc WHERE jc.member.id = :memberId")
    List<JoinedCrew> findByMemberId(Integer memberId);

    @Query("SELECT jc FROM JoinedCrew jc WHERE jc.crew.id = :crewId ORDER BY jc.isLeader DESC")
    List<JoinedCrew> findByCrewId(Long crewId);
}
