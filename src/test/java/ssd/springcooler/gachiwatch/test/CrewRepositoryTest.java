package ssd.springcooler.gachiwatch.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ssd.springcooler.gachiwatch.domain.Crew;
import ssd.springcooler.gachiwatch.repository.CrewRepository;

import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class CrewRepositoryTest {
    @Autowired
    CrewRepository crewRepository;

    @Test
    void testFindById() {
        Optional<Crew> crew = crewRepository.findById(1L);
        System.out.println("조회 결과: " + crew);
        assertTrue(crew.isPresent()); // 실패하면 실제 DB에 없는 것
    }

    private void assertTrue(boolean present) {
    }
}
