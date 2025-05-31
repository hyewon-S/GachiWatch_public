package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssd.springcooler.gachiwatch.domain.Crew;

import java.util.Optional;

public interface CrewRepository extends JpaRepository<Crew, Long> {
    //Crew findByCrewId(Long id);
    //Optional<Crew> findCrewByCrewId(Long id);
    Optional<Crew> findByCrewId(Long id);
}
