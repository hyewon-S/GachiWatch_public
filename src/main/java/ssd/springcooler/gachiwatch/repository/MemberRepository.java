package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssd.springcooler.gachiwatch.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    // 로그인할 때 이메일+비밀번호로 조회 (비밀번호는 보통 암호화 되어있어야 함)
    @Query("SELECT m FROM Member m WHERE m.email = :email AND m.password = :password")
    Optional<Member> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.preferredGenres WHERE m.email = :email")
    Optional<Member> findByEmailWithPreferredGenres(@Param("email") String email);

    boolean existsByEmail(String email);

}