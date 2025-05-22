package ssd.springcooler.gachiwatch.dao.mybatis.mapper;

import ssd.springcooler.gachiwatch.domain.Content;
import ssd.springcooler.gachiwatch.domain.Genre;
import ssd.springcooler.gachiwatch.domain.Platform;

import java.util.List;

public interface ContentMapper {

    void createContent (List<Content> content);

    List<Content> getContentList(List<Integer> ids);

    List<Content> findByKeyword(String keyword);

    Content findById(int id);

    void updateContent(Content content);

    void deleteContent(int id);

    List<Content> search(List<Integer> genre, List<Integer> platform, String content_type, String range);
}
