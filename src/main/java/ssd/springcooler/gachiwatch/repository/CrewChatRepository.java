package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ssd.springcooler.gachiwatch.domain.CrewChat;
import ssd.springcooler.gachiwatch.domain.CrewChatId;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CrewChatRepository extends JpaRepository<CrewChat, CrewChatId> {
    List<CrewChat> findByCrewIdOrderByChatDateAsc(Long crewId);
}
