package ssd.springcooler.gachiwatch.dao.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ssd.springcooler.gachiwatch.domain.Content;
import ssd.springcooler.gachiwatch.domain.Genre;
import ssd.springcooler.gachiwatch.domain.Platform;

import java.util.List;

@Mapper
public interface ContentMapper {

    void createContent (List<Content> content);

    List<Content> getContentList(List<Integer> ids);

    List<Content> findByKeyword(String keyword);

    Content findById(int id);

    void updateContent(Content content);

    void deleteContent(int id);

    List<Content> search(@Param("genre") List<Integer> genre, @Param("platform") List<Integer> platform, @Param("content_type") String content_type, @Param("range") String range);
}
