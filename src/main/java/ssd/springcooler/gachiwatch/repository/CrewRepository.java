package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.domain.Platform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface CrewRepository extends JpaRepository<Crew, Long> {
    //Crew findByCrewId(Long id);
    //Optional<Crew> findCrewByCrewId(Long id);
    Optional<Crew> findByCrewId(Long id);

    Page<Crew> findByPlatform(Platform platform, Pageable pageable);

    //@Query("select c from crew c where c.memberId like :memberId")
    //Page<Crew> findByCrewMembersMemberId(int memberId, Pageable pageable);

    //@Query("select * from crew c where c.platform like :platform")
    //List<Crew> findCrewByPlatform(Platform platform,);
}
