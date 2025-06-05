package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ssd.springcooler.gachiwatch.domain.CrewChat;
import ssd.springcooler.gachiwatch.domain.CrewChatId;

import java.util.List;

public interface CrewChatRepository extends JpaRepository<CrewChat, CrewChatId> {

    List<CrewChat> findByIdCrewId(Long id);

    @Query("SELECT c FROM CrewChat c WHERE c.id.crewId = :crewId")
    List<CrewChat> findAllByCrewId(@Param("crewId") Long crewId);
}
