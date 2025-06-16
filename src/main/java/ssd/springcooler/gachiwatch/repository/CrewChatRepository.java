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
    // 쿼리 메서드 방식 (권장)
    //List<CrewChat> findByCrewId(Long crewId);
    /*
    @Query("SELECT c FROM CrewChat c WHERE c.chatDate = :chatDate AND c.crew.id = :crewId AND c.member.id = :memberId")
    Optional<CrewChat> findOneByCompositeKey(@Param("chatDate") Date chatDate,
                                             @Param("crewId") Long crewId,
                                             @Param("memberId") Long memberId);

    @Query("SELECT c FROM CrewChat c WHERE c.crew.id = :crewId ORDER BY c.chatDate ASC")
    List<CrewChat> findByCrewIdJPQL(@Param("crewId") Long crewId);
*/
    // 특정 크루의 전체 채팅 조회
    List<CrewChat> findByCrewIdOrderByChatDateAsc(Long crewId);



}
