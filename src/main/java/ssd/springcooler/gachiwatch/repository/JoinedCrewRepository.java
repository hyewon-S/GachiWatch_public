package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ssd.springcooler.gachiwatch.domain.JoinedCrew;
import ssd.springcooler.gachiwatch.domain.JoinedCrewId;

import java.util.List;

public interface JoinedCrewRepository extends JpaRepository<JoinedCrew, JoinedCrewId> {
    //List<JoinedCrew> findByMemberMemberId(Long memberId);
    Page<JoinedCrew> findByMemberMemberId(int memberId, Pageable pageable);
}
