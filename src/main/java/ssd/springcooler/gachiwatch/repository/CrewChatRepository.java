package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ssd.springcooler.gachiwatch.domain.CrewChat;
import ssd.springcooler.gachiwatch.domain.CrewChatId;

import java.util.List;

public interface CrewChatRepository extends JpaRepository<CrewChat, Long> {

    List<CrewChat> findById_Crew_CrewIdOrderByIdAsc(Long id);
}
