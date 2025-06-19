package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssd.springcooler.gachiwatch.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Member> findByEmail(String email);
}