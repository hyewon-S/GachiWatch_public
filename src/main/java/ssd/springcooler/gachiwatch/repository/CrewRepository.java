package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ssd.springcooler.gachiwatch.domain.Crew;

import java.util.List;

public interface CrewRepository extends JpaRepository<Crew, Long> {
    Crew findByCrewId(Long id);
}
