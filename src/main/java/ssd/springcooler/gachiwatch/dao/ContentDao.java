package ssd.springcooler.gachiwatch.dao;
import ssd.springcooler.gachiwatch.domain.Content;
import ssd.springcooler.gachiwatch.dto.ContentSummaryDto;

import java.util.List;

public interface ContentDao {
    void createContent (List<Content> content);

    void insertGenre(int contentId, List<Integer> gid);

    void insertPlatform(int contentId, List<Integer> pid);

    List<Content> getContentList(List<Integer> ids);

    Content findById(int id);

    List<Content> getAllContentList();

    List<Integer> findAllContentIds();

    List<ContentSummaryDto> searchByKeyword(String keyword);
}
