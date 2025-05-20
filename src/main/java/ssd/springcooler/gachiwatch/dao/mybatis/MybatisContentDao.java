package ssd.springcooler.gachiwatch.dao.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ssd.springcooler.gachiwatch.dao.ContentDao;
import ssd.springcooler.gachiwatch.dao.mybatis.mapper.ContentMapper;
import ssd.springcooler.gachiwatch.domain.Content;
import ssd.springcooler.gachiwatch.domain.Genre;
import ssd.springcooler.gachiwatch.domain.Platform;

import java.util.List;

@Repository
public class MybatisContentDao implements ContentDao {
    @Autowired
    private ContentMapper contentMapper;

    @Override
    public void createContent (List<Content> content) {
        contentMapper.createContent(content);
    }

    @Override
    public List<Content> getContentList(List<Integer> ids) {
        return contentMapper.getContentList(ids);
    }

    @Override
    public List<Content> findByKeyword(String keyword) {
        return contentMapper.findByKeyword(keyword);
    }

    @Override
    public Content findById(int id) {
        return contentMapper.findById(id);
    }

    @Override
    public void updateContent(Content content) {
        contentMapper.updateContent(content);
    }

    @Override
    public void deleteContent(int id) {
        contentMapper.deleteContent(id);
    }

    @Override
    public List<Content> search(List<Genre> genre, List<Platform> platform, String content_type, String range) {
        return contentMapper.search(genre, platform, content_type, range);
    }
}
