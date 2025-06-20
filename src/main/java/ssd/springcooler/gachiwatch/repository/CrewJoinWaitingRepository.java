package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.CrewJoinWaiting;
import ssd.springcooler.gachiwatch.domain.CrewJoinWaitingId;

import java.util.List;
import java.util.Optional;

public interface CrewJoinWaitingRepository extends JpaRepository<CrewJoinWaiting, CrewJoinWaitingId> {
    List<CrewJoinWaiting> findByCrewId(Long crewId);
    void deleteByCrewIdAndUserId(Long crewId, Integer userId);
}
