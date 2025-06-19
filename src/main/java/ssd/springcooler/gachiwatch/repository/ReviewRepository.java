package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssd.springcooler.gachiwatch.domain.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByMember_MemberIdOrderByScoreDesc(int memberId);
    List<Review> findByMember_MemberIdOrderByScoreAsc(int memberId);
}

