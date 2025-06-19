package ssd.springcooler.gachiwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssd.springcooler.gachiwatch.domain.Content;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Integer> {
    List<Content> findAllByContentIdIn(List<Integer> contentIds);

}
