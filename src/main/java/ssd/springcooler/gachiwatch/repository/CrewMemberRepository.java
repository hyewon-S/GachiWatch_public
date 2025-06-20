package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.Member;

import java.util.List;

@Repository
public interface CrewMemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT m FROM Member m JOIN m.joinedCrews c WHERE c.crewId = :crewId")
    List<Member> findAllByCrewId(@Param("crewId") Long crewId);
}
